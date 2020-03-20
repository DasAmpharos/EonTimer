//
// Created by Dylan Meadows on 2020-03-17.
//

#include <QtCore>
#include "Gen4TimerSettings.h"

namespace service::settings {
    namespace Gen4Fields {
        const char *CALIBRATED_DELAY = "gen4/calibratedDelay";
        const char *CALIBRATED_SECOND = "gen4/calibratedSecond";
        const char *TARGET_DELAY = "gen4/targetDelay";
        const char *TARGET_SECOND = "gen4/targetSecond";
        
        namespace Defaults {
            const int CALIBRATED_DELAY = 500;
            const int CALIBRATED_SECOND = 14;
            const int TARGET_DELAY = 600;
            const int TARGET_SECOND = 50;
        }
    }

    Gen4TimerSettings::Gen4TimerSettings(QSettings *settings)
        : settings(settings) {
    }

    int Gen4TimerSettings::getCalibratedDelay() const {
        return settings->value(Gen4Fields::CALIBRATED_DELAY, Gen4Fields::Defaults::CALIBRATED_DELAY).toInt();
    }

    void Gen4TimerSettings::setCalibratedDelay(int calibratedDelay) {
        settings->setValue(Gen4Fields::CALIBRATED_DELAY, calibratedDelay);
    }

    int Gen4TimerSettings::getCalibratedSecond() const {
        return settings->value(Gen4Fields::CALIBRATED_SECOND, Gen4Fields::Defaults::CALIBRATED_SECOND).toInt();
    }

    void Gen4TimerSettings::setCalibratedSecond(int calibratedSecond) {
        settings->setValue(Gen4Fields::CALIBRATED_SECOND, calibratedSecond);
    }

    int Gen4TimerSettings::getTargetDelay() const {
        return settings->value(Gen4Fields::TARGET_DELAY, Gen4Fields::Defaults::TARGET_DELAY).toInt();
    }

    void Gen4TimerSettings::setTargetDelay(int targetDelay) {
        settings->setValue(Gen4Fields::TARGET_DELAY, targetDelay);
    }

    int Gen4TimerSettings::getTargetSecond() const {
        return settings->value(Gen4Fields::TARGET_SECOND, Gen4Fields::Defaults::TARGET_SECOND).toInt();
    }

    void Gen4TimerSettings::setTargetSecond(int targetSecond) {
        settings->setValue(Gen4Fields::TARGET_SECOND, targetSecond);
    }
}
