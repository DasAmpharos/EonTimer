//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_SECONDTIMER_H
#define EONTIMER_SECONDTIMER_H

#include <memory>
#include <vector>

namespace service::timer::SecondTimer {
    const std::shared_ptr<std::vector<int>> createStages(int targetSecond, int calibration);

    const int createStage1(int targetSecond, int calibration);

    int calibrate(int targetSecond, int secondHit);
}


#endif //EONTIMER_SECONDTIMER_H
