//
// Created by Dylan Meadows on 2020-03-12.
//

#include "SecondTimer.h"

#include <util/Functions.h>

namespace service::timer {
    const std::shared_ptr<std::vector<int>> SecondTimer::createStages(const int targetSecond,
                                                                      const int calibration) const {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(1);
        (*stages)[0] = createStage1(targetSecond, calibration);
        return stages;
    }

    int SecondTimer::createStage1(const int targetSecond, const int calibration) const {
        return util::functions::toMinimumLength(targetSecond * 1000 + calibration + 200);
    }

    int SecondTimer::calibrate(const int targetSecond, const int secondHit) const {
        if (secondHit < targetSecond) {
            return (targetSecond - secondHit) * 1000 - 500;
        } else if (secondHit > targetSecond) {
            return (targetSecond - secondHit) * 1000 + 500;
        } else {
            return 0;
        }
    }
}  // namespace service::timer
