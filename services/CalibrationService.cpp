//
// Created by Dylan Meadows on 2020-03-10.
//

#include "CalibrationService.h"
#include "SettingsService.h"
#include <models/Console.h>
#include <cmath>

namespace service::CalibrationService {
    int toDelays(const int milliseconds) {
        const double framerate = model::getFramerate(Settings::getConsole());
        return static_cast<int>(std::round(milliseconds / framerate));
    }

    int toMilliseconds(const int delays) {
        const double framerate = model::getFramerate(Settings::getConsole());
        return static_cast<int>(std::round(delays * framerate));
    }

    int createCalibration(const int delays, const int seconds) {
        return toMilliseconds(delays - toDelays(seconds * 1000));
    }
}