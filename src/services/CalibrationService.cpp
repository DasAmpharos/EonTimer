//
// Created by Dylan Meadows on 2020-03-10.
//

#include "CalibrationService.h"
#include <cmath>

namespace service {
    CalibrationService::CalibrationService(const model::settings::TimerSettingsModel *timerSettings)
        : timerSettings(timerSettings) {
    }

    int CalibrationService::toDelays(const int milliseconds) const {
        const double framerate = model::getFramerate(timerSettings->getConsole());
        return static_cast<int>(std::round(milliseconds / framerate));
    }

    int CalibrationService::toMilliseconds(const int delays) const {
        const double framerate = model::getFramerate(timerSettings->getConsole());
        return static_cast<int>(std::round(delays * framerate));
    }

    int CalibrationService::calibrateToDelays(const int milliseconds) const {
        return timerSettings->isPrecisionCalibrationEnabled()
               ? milliseconds : toDelays(milliseconds);
    }

    int CalibrationService::calibrateToMilliseconds(int delays) const {
        return timerSettings->isPrecisionCalibrationEnabled()
               ? delays : toMilliseconds(delays);
    }

    int CalibrationService::createCalibration(const int delays, const int seconds) const {
        return toMilliseconds(delays - toDelays(seconds * 1000));
    }
}