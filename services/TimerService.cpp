//
// Created by Dylan Meadows on 2020-03-10.
//

#include "TimerService.h"

namespace service {
    void TimerService::setCurrentStage(const long value) {
        if (currentStage != value) {
            currentStage = value;
            emit currentStageChanged(value);
        }
    }

    void TimerService::setMinutesBeforeTarget(const long value) {
        if (minutesBeforeTarget != value) {
            minutesBeforeTarget = value;
            emit minutesBeforeTargetChanged(value);
        }
    }

    void TimerService::setNextStage(const long value) {
        if (nextStage != value) {
            nextStage = value;
            emit nextStageChanged(value);
        }
    }
}