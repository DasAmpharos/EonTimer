//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_DELAYTIMER_H
#define EONTIMER_DELAYTIMER_H

#include <memory>
#include <vector>
#include <services/CalibrationService.h>
#include "SecondTimer.h"

namespace service::timer {
    class DelayTimer {
    private:
        const SecondTimer *secondTimer;
        const CalibrationService *calibrationService;
    public:
        explicit DelayTimer(const CalibrationService *calibrationService,
                            const SecondTimer *secondTimer);

        const std::shared_ptr<std::vector<int>> createStages(int targetSecond, int targetDelay, int calibration) const;

        const int createStage1(int targetSecond, int targetDelay, int calibration) const;

        const int createStage2(int targetDelay, int calibration) const;

        int calibrate(int targetDelay, int delayHit) const;
    };
}

#endif //EONTIMER_DELAYTIMER_H
