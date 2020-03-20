//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerSettings.h"

namespace service::settings {
    namespace Gen5Fields {
        const char *CALIBRATION = "gen5/calibration";
        const char *TARGET_DELAY = "gen5/targetDelay";
        const char *TARGET_SECOND = "gen5/targetSecond";
        const char *ENTRALINK_CALIBRATION = "gen5/entralinkCalibration";
        const char *FRAME_CALIBRATION = "gen5/frameCalibration";
        const char *TARGET_ADVANCES = "gen5/targetAdvances";

        namespace Defaults {
            const int CALIBRATION = 95;
            const int TARGET_DELAY = 1200;
            const int TARGET_SECOND = 50;
            const int ENTRALINK_CALIBRATION = 256;
            const int FRAME_CALIBRATION = 0;
            const int TARGET_ADVANCES = 100;
        }
    }

    Gen5TimerSettings::Gen5TimerSettings(QSettings *settings)
        : settings(settings) {
    }

    int Gen5TimerSettings::getCalibration() const {
        return settings->value(Gen5Fields::CALIBRATION, Gen5Fields::Defaults::CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setCalibration(int calibration) {
        settings->setValue(Gen5Fields::CALIBRATION, calibration);
    }

    int Gen5TimerSettings::getTargetDelay() const {
        return settings->value(Gen5Fields::TARGET_DELAY, Gen5Fields::Defaults::TARGET_DELAY).toInt();
    }

    void Gen5TimerSettings::setTargetDelay(int targetDelay) {
        settings->setValue(Gen5Fields::TARGET_DELAY, targetDelay);
    }

    int Gen5TimerSettings::getTargetSecond() const {
        return settings->value(Gen5Fields::TARGET_SECOND, Gen5Fields::Defaults::TARGET_SECOND).toInt();
    }

    void Gen5TimerSettings::setTargetSecond(int targetSecond) {
        settings->setValue(Gen5Fields::TARGET_SECOND, targetSecond);
    }

    int Gen5TimerSettings::getEntralinkCalibration() const {
        return settings->value(Gen5Fields::ENTRALINK_CALIBRATION, Gen5Fields::Defaults::ENTRALINK_CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setEntralinkCalibration(int entralinkCalibration) {
        settings->setValue(Gen5Fields::ENTRALINK_CALIBRATION, entralinkCalibration);
    }

    int Gen5TimerSettings::getFrameCalibration() const {
        return settings->value(Gen5Fields::FRAME_CALIBRATION, Gen5Fields::Defaults::FRAME_CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setFrameCalibration(int frameCalibration) {
        settings->setValue(Gen5Fields::FRAME_CALIBRATION, frameCalibration);
    }

    int Gen5TimerSettings::getTargetAdvances() const {
        return settings->value(Gen5Fields::TARGET_ADVANCES, Gen5Fields::Defaults::TARGET_ADVANCES).toInt();
    }

    void Gen5TimerSettings::setTargetAdvances(int targetAdvances) {
        settings->setValue(Gen5Fields::TARGET_ADVANCES, targetAdvances);
    }
}