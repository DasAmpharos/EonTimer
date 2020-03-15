//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CALIBRATIONHELPER_H
#define EONTIMER_CALIBRATIONHELPER_H

#include <services/settings/TimerSettings.h>

namespace service {
    class CalibrationService {
    private:
        const settings::TimerSettings *timerSettings;
    public:
        explicit CalibrationService(const settings::TimerSettings *timerSettings);

        int toDelays(int milliseconds) const;

        int toMilliseconds(int delays) const;

        int createCalibration(int delays, int seconds) const;
    };
}

#endif //EONTIMER_CALIBRATIONHELPER_H
