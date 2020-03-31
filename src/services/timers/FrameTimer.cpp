//
// Created by Dylan Meadows on 2020-03-20.
//

#include "FrameTimer.h"

namespace service::timer {
    FrameTimer::FrameTimer(const CalibrationService *calibrationService)
        : calibrationService(calibrationService) {
    }

    const std::shared_ptr<std::vector<int>>
    FrameTimer::createStages(const int preTimer, const int targetFrame, const int calibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(preTimer);
        (*stages)[1] = createStage2(targetFrame, calibration);
        return stages;
    }

    int FrameTimer::createStage1(const int preTimer) const {
        return preTimer;
    }

    int FrameTimer::createStage2(const int targetFrame, const int calibration) const {
        return calibrationService->toMilliseconds(targetFrame) + calibration;
    }

    int FrameTimer::calibrate(const int targetFrame, const int frameHit) const {
        return calibrationService->toMilliseconds(targetFrame - frameHit);
    }
}
