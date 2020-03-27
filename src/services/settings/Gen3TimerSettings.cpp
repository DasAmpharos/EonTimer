//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerSettings.h"

namespace service::settings {
    namespace Gen3Fields {
        const char *PRE_TIMER = "gen3/preTimer";
        const char *CALIBRATION = "gen3/calibration";
        const char *TARGET_FRAME = "gen3/targetFrame";

        namespace Defaults {
            const int PRE_TIMER = 5000;
            const int CALIBRATION = 0;
            const int TARGET_FRAME = 1000;
        }
    }

    Gen3TimerSettings::Gen3TimerSettings(QSettings *settings)
        : settings(settings) {
    }

    int Gen3TimerSettings::getCalibration() const {
        return settings->value(Gen3Fields::CALIBRATION, Gen3Fields::Defaults::CALIBRATION).toInt();
    }

    void Gen3TimerSettings::setCalibration(const int calibration) {
        settings->setValue(Gen3Fields::CALIBRATION, calibration);
    }

    int Gen3TimerSettings::getPreTimer() const {
        return settings->value(Gen3Fields::PRE_TIMER, Gen3Fields::Defaults::PRE_TIMER).toInt();
    }

    void Gen3TimerSettings::setPreTimer(int preTimer) {
        settings->setValue(Gen3Fields::PRE_TIMER, preTimer);
    }

    int Gen3TimerSettings::getTargetFrame() const {
        return settings->value(Gen3Fields::TARGET_FRAME, Gen3Fields::Defaults::TARGET_FRAME).toInt();
    }

    void Gen3TimerSettings::setTargetFrame(int targetFrame) {
        settings->setValue(Gen3Fields::TARGET_FRAME, targetFrame);
    }
}
