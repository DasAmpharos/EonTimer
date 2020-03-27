//
// Created by Dylan Meadows on 2020-03-22.
//

#include "EnhancedEntralinkTimer.h"
#include <cmath>

namespace service::timer {
    const double ENTRALINK_FRAME_RATE = 0.837148929;

    EnhancedEntralinkTimer::EnhancedEntralinkTimer(const EntralinkTimer *entralinkTimer)
        : entralinkTimer(entralinkTimer) {
    }

    const std::shared_ptr<std::vector<int>>
    EnhancedEntralinkTimer::createStages(int targetDelay,
                                         int targetSecond,
                                         int targetAdvances,
                                         int calibration,
                                         int entralinkCalibration,
                                         int frameCalibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(3);
        (*stages)[0] = createStage1(targetSecond, targetDelay, calibration);
        (*stages)[1] = createStage2(targetDelay, calibration, entralinkCalibration);
        (*stages)[2] = createStage3(targetAdvances, frameCalibration);
        return stages;
    }

    const int EnhancedEntralinkTimer::createStage1(int targetSecond, int targetDelay, int calibration) const {
        return entralinkTimer->createStage1(targetSecond, targetDelay, calibration);
    }

    const int EnhancedEntralinkTimer::createStage2(int targetDelay, int calibration, int entralinkCalibration) const {
        return entralinkTimer->createStage2(targetDelay, calibration, entralinkCalibration);
    }

    const int EnhancedEntralinkTimer::createStage3(int targetAdvances, int frameCalibration) const {
        return static_cast<const int>(round(targetAdvances / ENTRALINK_FRAME_RATE) * 1000 + frameCalibration);
    }

    int EnhancedEntralinkTimer::calibrate(int targetAdvances, int actualAdvances) const {
        return static_cast<int>((targetAdvances - actualAdvances) / ENTRALINK_FRAME_RATE) * 1000;
    }
}
