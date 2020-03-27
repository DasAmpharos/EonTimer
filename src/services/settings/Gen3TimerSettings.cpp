//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerSettings.h"

namespace service::settings {
    namespace Gen3Fields {
        const char *PRE_TIMER = "gen3/preTimer";
        const char *TARGET_FRAME = "gen3/targetFrame";
        const char *CALIBRATION = "gen3/calibration";

        namespace Defaults {
            const int PRE_TIMER = 5000;
            const int TARGET_FRAME = 1000;
            const int CALIBRATION = 0;
        }
    }

    Gen3TimerSettings::Gen3TimerSettings(QSettings *settings, QObject *parent)
        : QObject(parent),
          settings(settings) {
    }

    int Gen3TimerSettings::getPreTimer() const {
        return settings->value(Gen3Fields::PRE_TIMER, Gen3Fields::Defaults::PRE_TIMER).toInt();
    }

    void Gen3TimerSettings::setPreTimer(int preTimer) {
        if (getPreTimer() != preTimer) {
            settings->setValue(Gen3Fields::PRE_TIMER, preTimer);
            emit preTimerChanged(preTimer);
        }
    }

    int Gen3TimerSettings::getTargetFrame() const {
        return settings->value(Gen3Fields::TARGET_FRAME, Gen3Fields::Defaults::TARGET_FRAME).toInt();
    }

    void Gen3TimerSettings::setTargetFrame(int targetFrame) {
        if (getTargetFrame() != targetFrame) {
            settings->setValue(Gen3Fields::TARGET_FRAME, targetFrame);
            emit targetFrameChanged(targetFrame);
        }
    }

    int Gen3TimerSettings::getCalibration() const {
        return settings->value(Gen3Fields::CALIBRATION, Gen3Fields::Defaults::CALIBRATION).toInt();
    }

    void Gen3TimerSettings::setCalibration(const int calibration) {
        if (getCalibration() != calibration) {
            settings->setValue(Gen3Fields::CALIBRATION, calibration);
            emit calibrationChanged(calibration);
        }
    }
}
