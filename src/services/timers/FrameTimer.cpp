//
// Created by Dylan Meadows on 2020-03-20.
//

#include "FrameTimer.h"

namespace service::timer {
    FrameTimer::FrameTimer(CalibrationService *calibrationService)
        : calibrationService(calibrationService) {
    }

    const std::shared_ptr<std::vector<int>>
    FrameTimer::createStages(int preTimer, int targetFrame, int calibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(preTimer);
        (*stages)[1] = createStage2(targetFrame, calibration);
        return stages;
    }

    const int FrameTimer::createStage1(int preTimer) const {
        return preTimer;
    }

    const int FrameTimer::createStage2(int targetFrame, int calibration) const {
        return calibrationService->toMilliseconds(targetFrame) + calibration;
    }

    int FrameTimer::calibrate(int targetFrame, int frameHit) const {
        return calibrationService->toMilliseconds(targetFrame - frameHit);
    }
}
