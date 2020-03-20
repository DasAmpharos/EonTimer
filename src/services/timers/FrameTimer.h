//
// Created by Dylan Meadows on 2020-03-20.
//

#ifndef EONTIMER_FRAMETIMER_H
#define EONTIMER_FRAMETIMER_H

#include <services/CalibrationService.h>

namespace service::timer {
    class FrameTimer {
    private:
        CalibrationService *calibrationService;
    public:
        explicit FrameTimer(CalibrationService *calibrationService);

        const std::shared_ptr<std::vector<int>> createStages(int preTimer, int targetFrame, int calibration) const;

        const int createStage1(int preTimer) const;

        const int createStage2(int targetFrame, int calibration) const;

        int calibrate(int targetFrame, int frameHit) const;
    };
}

#endif //EONTIMER_FRAMETIMER_H
