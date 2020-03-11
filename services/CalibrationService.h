//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CALIBRATIONHELPER_H
#define EONTIMER_CALIBRATIONHELPER_H

#include <cmath>
#include "../models/Console.h"
#include "SettingsService.h"

namespace service {
    class CalibrationService {
    private:
        const SettingsService *settings;
    public:
        explicit CalibrationService(SettingsService *settings);

        long toDelays(long milliseconds) const;

        long toMilliseconds(long delays) const;

        long createCalibration(long delays, long seconds) const;
    };
}

#endif //EONTIMER_CALIBRATIONHELPER_H
