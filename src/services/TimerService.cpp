#include <utility>

//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"
#include "SoundService.h"
#include <QThreadPool>
#include <stack>

using namespace std::literals::chrono_literals;

namespace service {
    TimerService::TimerService(model::settings::TimerSettingsModel *timerSettings,
                               model::settings::ActionSettingsModel *actionSettings,
                               QObject *parent)
        : QObject(parent),
          running(false),
          timerSettings(timerSettings),
          actionSettings(actionSettings) {
        auto *sounds = new SoundService(actionSettings, this);
        connect(this, &TimerService::actionTriggered, [sounds] {
            sounds->play();
        });
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
        auto preElapsed = 0ms;
        uint8_t stageIndex = 0;
        while (running && stageIndex < stages->size()) {
            auto currentStage = std::chrono::milliseconds((*stages)[stageIndex]);
            preElapsed = runStage(stageIndex, preElapsed) - currentStage;
            stageIndex++;
        }
        running = false;
        emit activated(false);
        reset();
    }

    std::chrono::milliseconds
    TimerService::runStage(const uint8_t stageIndex, const std::chrono::milliseconds preElapsed) {
        const auto period = timerSettings->getRefreshInterval();
        const auto currentStage = std::chrono::milliseconds((*stages)[stageIndex]);

        std::stack<std::chrono::milliseconds> actionStack;
        const auto actionInterval = actionSettings->getInterval();
        for (uint i = 0; i < actionSettings->getCount(); i++) {
            actionStack.push(actionInterval * i);
        }

        auto ticks = 0;
        auto elapsed = preElapsed;
        auto adjustedPeriod = period;
        auto lastTimestamp = std::chrono::high_resolution_clock::now();
        while (running && elapsed < currentStage) {
            const auto nextAction = actionStack.top();
            const auto remainingUntilAction = currentStage - elapsed - nextAction;
            if (remainingUntilAction < adjustedPeriod) adjustedPeriod = remainingUntilAction;
            std::this_thread::sleep_for(std::chrono::duration_cast<std::chrono::nanoseconds>(adjustedPeriod));

            const auto now = std::chrono::high_resolution_clock::now();
            const auto delta = std::chrono::duration_cast<std::chrono::milliseconds>(now - lastTimestamp);
            const auto remaining = currentStage - elapsed - delta;
            if (remaining <= nextAction) {
                emit actionTriggered();
                actionStack.pop();
            }
            if (ticks % 4 == 0) emit stateChanged(model::TimerState(currentStage, remaining));
            adjustedPeriod -= delta - period;
            lastTimestamp = now;
            elapsed += delta;
            ticks++;
        }
        return elapsed;
    }

    bool TimerService::isRunning() const {
        return running;
    }
}