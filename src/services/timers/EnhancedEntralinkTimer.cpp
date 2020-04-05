//
// Created by Dylan Meadows on 2020-03-22.
//

#include "EnhancedEntralinkTimer.h"

#include <cmath>

namespace service::timer {
    const double ENTRALINK_FRAME_RATE = 0.837148929;

    EnhancedEntralinkTimer::EnhancedEntralinkTimer(
        const EntralinkTimer *entralinkTimer)
        : entralinkTimer(entralinkTimer) {}

    const std::shared_ptr<std::vector<int>>
    EnhancedEntralinkTimer::createStages(const int targetDelay,
                                         const int targetSecond,
                                         const int targetAdvances,
                                         const int calibration,
                                         const int entralinkCalibration,
                                         const int frameCalibration) const {
        std::shared_ptr<std::vector<int>> stages =
            std::make_shared<std::vector<int>>(3);
        (*stages)[0] = createStage1(targetDelay, targetSecond, calibration);
        (*stages)[1] =
            createStage2(targetDelay, calibration, entralinkCalibration);
        (*stages)[2] = createStage3(targetAdvances, frameCalibration);
        return stages;
    }

    int EnhancedEntralinkTimer::createStage1(const int targetDelay,
                                             const int targetSecond,
                                             const int calibration) const {
        return entralinkTimer->createStage1(targetDelay, targetSecond,
                                            calibration);
    }

    int EnhancedEntralinkTimer::createStage2(
        const int targetDelay, const int calibration,
        const int entralinkCalibration) const {
        return entralinkTimer->createStage2(targetDelay, calibration,
                                            entralinkCalibration);
    }

    int EnhancedEntralinkTimer::createStage3(const int targetAdvances,
                                             const int frameCalibration) const {
        return static_cast<int>(round(targetAdvances / ENTRALINK_FRAME_RATE) *
                                    1000 +
                                frameCalibration);
    }

    int EnhancedEntralinkTimer::calibrate(int targetAdvances,
                                          int actualAdvances) const {
        return static_cast<int>((targetAdvances - actualAdvances) /
                                ENTRALINK_FRAME_RATE) *
               1000;
    }
}  // namespace service::timer
