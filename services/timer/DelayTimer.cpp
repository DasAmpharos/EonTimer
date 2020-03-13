//
// Created by Dylan Meadows on 2020-03-12.
//

#include "DelayTimer.h"
#include "SecondTimer.h"
#include <util/Functions.h>
#include <services/CalibrationService.h>

namespace service::timer::DelayTimer {
    const int CLOSE_THRESHOLD = 167;
    const double UPDATE_FACTOR = 1.0;
    const double CLOSE_UPDATE_FACTOR = 0.75;

    const std::shared_ptr<std::vector<int>>
    createStages(const int targetSecond, const int targetDelay, const int calibration) {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(targetSecond, targetDelay, calibration);
        (*stages)[1] = createStage2(targetDelay, calibration);
        return stages;
    }

    const int createStage1(const int targetSecond, const int targetDelay, const int calibration) {
        return util::functions::toMinimumLength(
            SecondTimer::createStage1(targetSecond, calibration) -
            CalibrationService::toMilliseconds(targetDelay)
        );
    }

    const int createStage2(const int targetDelay, const int calibration) {
        return CalibrationService::toMilliseconds(targetDelay) - calibration;
    }

    int calibrate(const int targetDelay, const int delayHit) {
        const int delta = CalibrationService::toMilliseconds(delayHit) -
                          CalibrationService::toMilliseconds(targetDelay);
        if (std::abs(delta) <= CLOSE_THRESHOLD) {
            return static_cast<int>(CLOSE_UPDATE_FACTOR * delta);
        } else {
            return static_cast<int>(UPDATE_FACTOR * delta);
        }
    }
}
