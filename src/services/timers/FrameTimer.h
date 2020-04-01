//
// Created by Dylan Meadows on 2020-03-20.
//

#ifndef EONTIMER_FRAMETIMER_H
#define EONTIMER_FRAMETIMER_H

#include <services/CalibrationService.h>
#include <memory>
#include <vector>

namespace service::timer {
    class FrameTimer {
    private:
        const CalibrationService *calibrationService;
    public:
        explicit FrameTimer(const CalibrationService *calibrationService);

        const std::shared_ptr<std::vector<int>> createStages(int preTimer, int targetFrame, int calibration) const;

        int createStage1(int preTimer) const;

        int createStage2(int targetFrame, int calibration) const;

        int calibrate(int targetFrame, int frameHit) const;
    };
}

#endif //EONTIMER_FRAMETIMER_H
