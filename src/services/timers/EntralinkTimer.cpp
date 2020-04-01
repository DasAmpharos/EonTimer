//
// Created by Dylan Meadows on 2020-03-20.
//

#include "EntralinkTimer.h"

namespace service::timer {
    EntralinkTimer::EntralinkTimer(const DelayTimer *delayTimer)
        : delayTimer(delayTimer) {
    }

    const std::shared_ptr<std::vector<int>>
    EntralinkTimer::createStages(const int targetDelay, const int targetSecond, const int calibration, const int entralinkCalibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(targetDelay, targetSecond, calibration);
        (*stages)[1] = createStage2(targetDelay, calibration, entralinkCalibration);
        return stages;
    }

    int EntralinkTimer::createStage1(const int targetDelay, const int targetSecond, const int calibration) const {
        return delayTimer->createStage1(targetDelay, targetSecond, calibration) + 250;
    }

    int EntralinkTimer::createStage2(const int targetDelay, const int calibration, const int entralinkCalibration) const {
        return delayTimer->createStage2(targetDelay, calibration) - entralinkCalibration;
    }

    int EntralinkTimer::calibrate(const int targetDelay, const int delayHit) const {
        return delayTimer->calibrate(targetDelay, delayHit);
    }
}
