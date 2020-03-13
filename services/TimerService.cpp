#include <utility>

//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"
#include <models/TimerState.h>

namespace service {
    void TimerService::start() {
        if (!running) {
            running = true;
            publishStateChange();
        }
    }

    void TimerService::stop() {
        if (running) {
            running = false;
            reset();
        }
    }

    void TimerService::setStages(std::shared_ptr<std::vector<int>> stages) {
        if (!running) {
            this->stages = std::move(stages);
            reset();
        }
    }

    const bool TimerService::isRunning() const {
        return running;
    }

    void TimerService::reset() {
        elapsed = 0;
        currentStageIdx = 0;

        publishStateChange();

        int totalTime = 0;
        for (int stage : (*stages)) {
            totalTime += stage;
        }
        const int minutesBeforeTarget = totalTime / 60000;
        emit minutesBeforeTargetChanged(minutesBeforeTarget);

        if (stages->size() >= 2) {
            emit nextStageChanged((*stages)[1]);
        } else {
            emit nextStageChanged(0);
        }
    }

    void TimerService::publishStateChange() {
        int currentStage = (*stages)[currentStageIdx];
        const model::TimerState state(currentStage, currentStage - elapsed, running);
        emit stateChanged(state);
    }
}