//
// Created by Dylan Meadows on 2020-03-12.
//

#include "DelayTimer.h"

#include <util/Functions.h>

namespace service::timer {
    const int CLOSE_THRESHOLD = 167;
    const double UPDATE_FACTOR = 1.0;
    const double CLOSE_UPDATE_FACTOR = 0.75;

    DelayTimer::DelayTimer(const SecondTimer *secondTimer, const CalibrationService *calibrationService)
        : secondTimer(secondTimer), calibrationService(calibrationService) {}

    const std::shared_ptr<std::vector<int>> DelayTimer::createStages(const int targetDelay,
                                                                     const int targetSecond,
                                                                     const int calibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(2);
        (*stages)[0] = createStage1(targetDelay, targetSecond, calibration);
        (*stages)[1] = createStage2(targetDelay, calibration);
        return stages;
    }

    int DelayTimer::createStage1(const int targetDelay, const int targetSecond, const int calibration) const {
        return util::functions::toMinimumLength(secondTimer->createStage1(targetSecond, calibration) -
                                                calibrationService->toMilliseconds(targetDelay));
    }

    int DelayTimer::createStage2(const int targetDelay, const int calibration) const {
        return calibrationService->toMilliseconds(targetDelay) - calibration;
    }

    int DelayTimer::calibrate(const int targetDelay, const int delayHit) const {
        const int delta =
            calibrationService->toMilliseconds(delayHit) - calibrationService->toMilliseconds(targetDelay);
        if (std::abs(delta) <= CLOSE_THRESHOLD) {
            return static_cast<int>(CLOSE_UPDATE_FACTOR * delta);
        } else {
            return static_cast<int>(UPDATE_FACTOR * delta);
        }
    }
}  // namespace service::timer
