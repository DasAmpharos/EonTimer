//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_DELAYTIMER_H
#define EONTIMER_DELAYTIMER_H

#include "SecondTimer.h"
#include <services/CalibrationService.h>
#include <memory>
#include <vector>

namespace service::timer {
    class DelayTimer {
    private:
        const SecondTimer *secondTimer;
        const CalibrationService *calibrationService;
    public:
        DelayTimer(const SecondTimer *secondTimer,
                   const CalibrationService *calibrationService);

        const std::shared_ptr<std::vector<int>> createStages(int targetDelay, int targetSecond, int calibration) const;

        int createStage1(int targetDelay, int targetSecond, int calibration) const;

        int createStage2(int targetDelay, int calibration) const;

        int calibrate(int targetDelay, int delayHit) const;
    };
}

#endif //EONTIMER_DELAYTIMER_H
