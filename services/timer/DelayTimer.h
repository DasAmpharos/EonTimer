//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_DELAYTIMER_H
#define EONTIMER_DELAYTIMER_H

#include <memory>
#include <vector>

namespace service::timer::DelayTimer {
    const std::shared_ptr<std::vector<int>> createStages(int targetSecond, int targetDelay, int calibration);

    const int createStage1(int targetSecond, int targetDelay, int calibration);

    const int createStage2(int targetDelay, int calibration);

    int calibrate(int targetDelay, int delayHit);
}

#endif //EONTIMER_DELAYTIMER_H
