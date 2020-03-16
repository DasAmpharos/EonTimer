#include <utility>

//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"
#include "SoundService.h"
#include <QThreadPool>
#include <util/QRunnableFunction.h>
#include <stack>

using namespace std::literals::chrono_literals;

namespace service {
    TimerService::TimerService(settings::TimerSettings *timerSettings,
                               settings::ActionSettings *actionSettings,
                               QObject *parent)
        : QObject(parent),
          timerSettings(timerSettings),
          actionSettings(actionSettings),
          running(false) {
        auto *sounds = new service::SoundService(actionSettings);
        connect(this, &TimerService::actionTriggered, [sounds]() {
            sounds->play();
        });
    }

    TimerService::~TimerService() {
        running = false;
    }

    void TimerService::setStages(std::shared_ptr<std::vector<int>> stages) {
        if (!running) {
            this->stages = std::move(stages);
            reset();
        }
    }

    void TimerService::start() {
        if (!running) {
            QThreadPool::globalInstance()->start(
                new util::QRunnableFunction([this]() {
                    running = true;
                    emit activated(true);
                    run();
                }));
        }
    }

    void TimerService::stop() {
        if (running) {
            running = false;
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
        stop();
    }

    std::chrono::milliseconds TimerService::runStage(uint8_t stageIndex, std::chrono::milliseconds preElapsed) {
        const auto period = timerSettings->getRefreshInterval();
        const auto currentStage = std::chrono::milliseconds((*stages)[stageIndex]);

        std::stack<std::chrono::milliseconds> actionStack;
        const auto actionInterval = actionSettings->getInterval();
        for (uint i = 0; i < actionSettings->getCount(); i++) {
            actionStack.push(actionInterval * i);
        }

        auto elapsed = preElapsed;
        auto adjustedPeriod = period;
        auto lastTimestamp = std::chrono::high_resolution_clock::now();
        while (running && elapsed < currentStage) {
            std::this_thread::sleep_for(std::chrono::duration_cast<std::chrono::nanoseconds>(adjustedPeriod));

            const auto now = std::chrono::high_resolution_clock::now();
            const auto delta = std::chrono::duration_cast<std::chrono::milliseconds>(now - lastTimestamp);
            adjustedPeriod -= delta - period;
            lastTimestamp = now;
            elapsed += delta;

            emit stateChanged(model::TimerState(currentStage, currentStage - elapsed));
            if (currentStage - elapsed <= actionStack.top()) {
                emit actionTriggered();
                actionStack.pop();
            }
        }
        return elapsed;
    }

    bool TimerService::isRunning() const {
        return running;
    }
}