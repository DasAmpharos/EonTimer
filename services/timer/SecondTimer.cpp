//
// Created by Dylan Meadows on 2020-03-12.
//

#include "SecondTimer.h"
#include <util/Functions.h>

namespace service::timer::SecondTimer {
    const std::shared_ptr<std::vector<int>>
    createStages(const int targetSecond, const int calibration) {
        std::shared_ptr<std::vector<int>> stages = std::make_shared<std::vector<int>>(1);
        (*stages)[0] = createStage1(targetSecond, calibration);
        return stages;
    }

    const int createStage1(const int targetSecond, const int calibration) {
        return util::functions::toMinimumLength(targetSecond * 1000 + calibration + 200);
    }

    int calibrate(const int targetSecond, const int secondHit) {
        if (secondHit < targetSecond) {
            return (targetSecond - secondHit) * 1000 - 500;
        } else if (secondHit > targetSecond) {
            return (targetSecond - secondHit) * 1000 + 500;
        } else {
            return 0;
        }
    }
}
