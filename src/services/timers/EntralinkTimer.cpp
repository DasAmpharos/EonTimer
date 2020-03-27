//
// Created by Dylan Meadows on 2020-03-20.
//

#include "EntralinkTimer.h"

namespace service::timer {
    EntralinkTimer::EntralinkTimer(const DelayTimer *delayTimer)
        : delayTimer(delayTimer) {
    }

    const std::shared_ptr<std::vector<int>>
    EntralinkTimer::createStages(int targetSecond, int targetDelay, int calibration, int entralinkCalibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(targetSecond, targetDelay, calibration);
        (*stages)[1] = createStage2(targetDelay, calibration, entralinkCalibration);
        return stages;
    }

    const int EntralinkTimer::createStage1(int targetSecond, int targetDelay, int calibration) const {
        return delayTimer->createStage1(targetSecond, targetDelay, calibration) + 250;
    }

    const int EntralinkTimer::createStage2(int targetDelay, int calibration, int entralinkCalibration) const {
        return delayTimer->createStage2(targetDelay, calibration) - entralinkCalibration;
    }

    int EntralinkTimer::calibrate(int targetDelay, int delayHit) const {
        return delayTimer->calibrate(targetDelay, delayHit);
    }
}
