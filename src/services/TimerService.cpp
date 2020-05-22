//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"

#include <util/Clock.h>
#include <util/Functions.h>

#include <QThreadPool>
#include <stack>
#include <utility>

#include "SoundService.h"

using namespace std::literals::chrono_literals;

namespace service {
    TimerService::TimerService(model::settings::TimerSettingsModel *timerSettings,
                               model::settings::ActionSettingsModel *actionSettings,
                               QObject *parent)
        : QObject(parent), running(false), timerSettings(timerSettings), actionSettings(actionSettings) {
        auto *sounds = new SoundService(actionSettings, this);
        connect(this, &TimerService::actionTriggered, [sounds] { sounds->play(); });
    }

    TimerService::~TimerService() {
        if (running) {
            running = false;
            timerThread->quit();
            timerThread->wait();
        }
    }

    void TimerService::setStages(std::shared_ptr<std::vector<int>> stages) {
        if (!running) {
            this->stages = std::move(stages);
            reset();
        }
    }

    void TimerService::start() {
        if (!running) {
            running = true;
            timerThread = QThread::create(&TimerService::run, this);
            timerThread->start();
            emit activated(true);
        }
    }

    void TimerService::stop() {
        if (running) {
            running = false;
            timerThread->quit();
            timerThread->wait();
            emit activated(false);
            reset();
        }
    }

    void TimerService::reset() {
        auto totalTime = 0ms;
        for (int stage : (*stages)) {
            totalTime += std::chrono::milliseconds(stage);
        }
        const auto currentStage = std::chrono::milliseconds((*stages)[0]);
        emit stateChanged(model::TimerState(currentStage, currentStage));
        emit minutesBeforeTargetChanged(std::chrono::duration_cast<std::chrono::minutes>(totalTime));
        emit nextStageChanged(stages->size() >= 2 ? std::chrono::milliseconds((*stages)[1]) : 0ms);
    }

    void TimerService::run() {
        auto preElapsed = 0us;
        uint8_t stageIndex = 0;
        while (running && stageIndex < stages->size()) {
            auto currentStage =
                std::chrono::duration_cast<std::chrono::microseconds>(std::chrono::milliseconds((*stages)[stageIndex]));
            preElapsed = runStage(currentStage, preElapsed) - currentStage;
            stageIndex++;
        }

        running = false;
        emit activated(false);
        reset();
    }

    std::chrono::microseconds TimerService::runStage(const std::chrono::microseconds stage,
                                                     const std::chrono::microseconds preElapsed) {
        util::Clock clock;
        const auto period = std::chrono::duration_cast<std::chrono::microseconds>(timerSettings->getRefreshInterval());

        std::stack<std::chrono::microseconds> actionStack;
        const auto actionInterval = std::chrono::milliseconds(actionSettings->getInterval());
        for (uint i = 0; i < actionSettings->getCount(); i++) {
            actionStack.push(std::chrono::duration_cast<std::chrono::microseconds>(actionInterval * i));
        }

        auto ticks = 0;
        auto elapsed = preElapsed;
        auto adjustedPeriod = period;
        auto nextAction = actionStack.top();
        while (running && elapsed < stage) {
            const auto remainingUntilAction = stage - elapsed - nextAction;
            if (remainingUntilAction < adjustedPeriod) adjustedPeriod = remainingUntilAction;
            std::this_thread::sleep_for(adjustedPeriod);

            const auto delta = clock.tick();
            const auto remaining = stage - elapsed - delta;
            if (remaining <= nextAction) {
                emit actionTriggered();
                actionStack.pop();
                if (!actionStack.empty()) {
                    nextAction = actionStack.top();
                }
            }
            if (ticks % 4 == 0)
                emit stateChanged(model::TimerState(std::chrono::duration_cast<std::chrono::milliseconds>(stage),
                                                    std::chrono::duration_cast<std::chrono::milliseconds>(remaining)));
            adjustedPeriod -= delta - period;
            elapsed += delta;
            ticks++;
        }
        return elapsed;
    }

    bool TimerService::isRunning() const { return running; }
}  // namespace service