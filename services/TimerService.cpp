#include <utility>

//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"
#include "SettingsService.h"
#include "SoundService.h"
#include <models/TimerState.h>
#include <models/Sound.h>
#include <iostream>
#include <stack>

using namespace std::literals::chrono_literals;

namespace service {

    TimerService::TimerService(SoundService *sounds, QObject *parent)
        : QObject(parent),
          sounds(sounds),
          running(false),
          timerThread(new TimerThread(this)) {
    }

    TimerService::~TimerService() {
        stop();
    }

    void TimerService::start() {
        if (!running) {
            running = true;
            timerThread->submit(std::bind(&TimerService::run, this));
        }
    }

    void TimerService::stop() {
        if (running) {
            running = false;
        }
    }

    void TimerService::run() {
        uint8_t stageIndex = 0;
        auto preElapsed = std::chrono::milliseconds(0);
        while (running && stageIndex < stages->size()) {
            auto currentStage = std::chrono::milliseconds((*stages)[stageIndex]);
            preElapsed = runStage(stageIndex, preElapsed) - currentStage;
            stageIndex++;
        }
        running = false;
        reset();
    }

    std::chrono::milliseconds
    TimerService::runStage(const uint8_t stageIndex, const std::chrono::milliseconds preElapsed) {
        auto settings = SettingsService::get();
        const auto period = settings.getRefreshInterval();
        const auto currentStage = std::chrono::milliseconds((*stages)[stageIndex]);

        std::stack<std::chrono::milliseconds> actionStack;
        const auto actionInterval = settings.getActionInterval();
        for (uint8_t i = 0; i < settings.getActionCount(); i++) {
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

            publishStateChange(currentStage, elapsed);
            if (currentStage - elapsed <= actionStack.top()) {
                sounds->play(model::Sound::BEEP);
                actionStack.pop();
            }
        }
        return elapsed;
    }

    void TimerService::reset() {
        const auto currentStage = std::chrono::milliseconds((*stages)[0]);
        publishStateChange(currentStage, 0ms);

        auto totalTime = 0ms;
        for (int stage : (*stages)) {
            totalTime += std::chrono::milliseconds(stage);
        }
        emit minutesBeforeTargetChanged(std::chrono::duration_cast<std::chrono::minutes>(totalTime));

        if (stages->size() >= 2) {
            emit nextStageChanged(std::chrono::milliseconds((*stages)[1]));
        } else {
            emit nextStageChanged(0ms);
        }
    }

    void TimerService::publishStateChange(const std::chrono::milliseconds &currentStage,
                                          const std::chrono::milliseconds &elapsed) {
        const model::TimerState state(currentStage, currentStage - elapsed);
        emit stateChanged(state);
    }

    void TimerService::setStages(std::shared_ptr<std::vector<int>> stages) {
        if (!running) {
            this->stages = std::move(stages);
            reset();
        }
    }

    bool TimerService::isRunning() const {
        return running;
    }
}