//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerSettings.h"

namespace service::settings {
    namespace Gen5Fields {
        const char *MODE = "gen5/mode";
        const char *CALIBRATION = "gen5/calibration";
        const char *TARGET_DELAY = "gen5/targetDelay";
        const char *TARGET_SECOND = "gen5/targetSecond";
        const char *ENTRALINK_CALIBRATION = "gen5/entralinkCalibration";
        const char *FRAME_CALIBRATION = "gen5/frameCalibration";
        const char *TARGET_ADVANCES = "gen5/targetAdvances";

        namespace Defaults {
            const int MODE = 0;
            const int CALIBRATION = 95;
            const int TARGET_DELAY = 1200;
            const int TARGET_SECOND = 50;
            const int ENTRALINK_CALIBRATION = 256;
            const int FRAME_CALIBRATION = 0;
            const int TARGET_ADVANCES = 100;
        }
    }

    Gen5TimerSettings::Gen5TimerSettings(QSettings *settings, QObject *parent)
        : QObject(parent),
          settings(settings) {
    }

    model::Gen5TimerMode Gen5TimerSettings::getMode() const {
        const int index = settings->value(Gen5Fields::MODE, Gen5Fields::Defaults::MODE).toInt();
        return model::gen5TimerModes()[index];
    }

    void Gen5TimerSettings::setMode(model::Gen5TimerMode mode) {
        if (getMode() != mode) {
            settings->setValue(Gen5Fields::MODE, model::indexOf(mode));
            emit modeChanged(mode);
        }
    }

    int Gen5TimerSettings::getCalibration() const {
        return settings->value(Gen5Fields::CALIBRATION, Gen5Fields::Defaults::CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setCalibration(int calibration) {
        if (getCalibration() != calibration) {
            settings->setValue(Gen5Fields::CALIBRATION, calibration);
            emit calibrationChanged(calibration);
        }
    }

    int Gen5TimerSettings::getTargetDelay() const {
        return settings->value(Gen5Fields::TARGET_DELAY, Gen5Fields::Defaults::TARGET_DELAY).toInt();
    }

    void Gen5TimerSettings::setTargetDelay(int targetDelay) {
        if (getTargetDelay() != targetDelay) {
            settings->setValue(Gen5Fields::TARGET_DELAY, targetDelay);
            emit targetDelayChanged(targetDelay);
        }
    }

    int Gen5TimerSettings::getTargetSecond() const {
        return settings->value(Gen5Fields::TARGET_SECOND, Gen5Fields::Defaults::TARGET_SECOND).toInt();
    }

    void Gen5TimerSettings::setTargetSecond(int targetSecond) {
        if (getTargetSecond() != targetSecond) {
            settings->setValue(Gen5Fields::TARGET_SECOND, targetSecond);
            emit targetSecondChanged(targetSecond);
        }
    }

    int Gen5TimerSettings::getEntralinkCalibration() const {
        return settings->value(Gen5Fields::ENTRALINK_CALIBRATION, Gen5Fields::Defaults::ENTRALINK_CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setEntralinkCalibration(int entralinkCalibration) {
        if (getEntralinkCalibration() != entralinkCalibration) {
            settings->setValue(Gen5Fields::ENTRALINK_CALIBRATION, entralinkCalibration);
            emit entralinkCalibrationChanged(entralinkCalibration);
        }
    }

    int Gen5TimerSettings::getFrameCalibration() const {
        return settings->value(Gen5Fields::FRAME_CALIBRATION, Gen5Fields::Defaults::FRAME_CALIBRATION).toInt();
    }

    void Gen5TimerSettings::setFrameCalibration(int frameCalibration) {
        if (getFrameCalibration() != frameCalibration) {
            settings->setValue(Gen5Fields::FRAME_CALIBRATION, frameCalibration);
            emit frameCalibrationChanged(frameCalibration);
        }
    }

    int Gen5TimerSettings::getTargetAdvances() const {
        return settings->value(Gen5Fields::TARGET_ADVANCES, Gen5Fields::Defaults::TARGET_ADVANCES).toInt();
    }

    void Gen5TimerSettings::setTargetAdvances(int targetAdvances) {
        if (getTargetAdvances() != targetAdvances) {
            settings->setValue(Gen5Fields::TARGET_ADVANCES, targetAdvances);
            emit targetAdvancesChanged(targetAdvances);
        }
    }
}