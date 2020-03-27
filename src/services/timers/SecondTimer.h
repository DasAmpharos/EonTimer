//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_SECONDTIMER_H
#define EONTIMER_SECONDTIMER_H

#include <memory>
#include <vector>

namespace service::timer {
    class SecondTimer {
    public:
        const std::shared_ptr<std::vector<int>> createStages(int targetSecond, int calibration) const;

        const int createStage1(int targetSecond, int calibration) const;

        int calibrate(int targetSecond, int secondHit) const;
    };
}


#endif //EONTIMER_SECONDTIMER_H
