//
// Created by Dylan Meadows on 2020-03-10.
//

#include "CalibrationService.h"

namespace service {
    CalibrationService::CalibrationService(SettingsService *settings)
        : settings(settings) {
    }

    long CalibrationService::toDelays(const long milliseconds) const {
        const double framerate = model::getFramerate(settings->getConsole());
        return static_cast<long>(std::round(
            milliseconds / framerate
        ));
    }

    long CalibrationService::toMilliseconds(const long delays) const {
        const double framerate = model::getFramerate(settings->getConsole());
        return static_cast<long>(std::round(
            delays * framerate
        ));
    }

    long CalibrationService::createCalibration(const long delays, const long seconds) const {
        return toMilliseconds(delays - toDelays(seconds * 1000));
    }
}