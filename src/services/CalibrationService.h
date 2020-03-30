//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CALIBRATIONSERVICE_H
#define EONTIMER_CALIBRATIONSERVICE_H

#include <models/settings/TimerSettingsModel.h>

namespace service {
    class CalibrationService {
    private:
        const model::settings::TimerSettingsModel *timerSettings;
    public:
        explicit CalibrationService(const model::settings::TimerSettingsModel *timerSettings);

        int toDelays(int milliseconds) const;

        int toMilliseconds(int delays) const;

        int createCalibration(int delays, int seconds) const;
    };
}

#endif //EONTIMER_CALIBRATIONSERVICE_H
