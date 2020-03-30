//
// Created by Dylan Meadows on 2020-03-26.
//

#include "Gen3TimerModel.h"

namespace model::timer {
    namespace Gen3Fields {
        const char *GROUP = "gen3";
        const char *PRE_TIMER = "preTimer";
        const char *TARGET_FRAME = "targetFrame";
        const char *CALIBRATION = "calibration";

        namespace Defaults {
            const int PRE_TIMER = 5000;
            const int TARGET_FRAME = 1000;
            const int CALIBRATION = 0;
        }
    }

    Gen3TimerModel::Gen3TimerModel(QSettings *settings, QObject *parent)
        : QObject(parent) {
        settings->beginGroup(Gen3Fields::GROUP);
        preTimer = settings->value(Gen3Fields::PRE_TIMER,
                                   Gen3Fields::Defaults::PRE_TIMER).toInt();
        targetFrame = settings->value(Gen3Fields::TARGET_FRAME,
                                      Gen3Fields::Defaults::TARGET_FRAME).toInt();
        calibration = settings->value(Gen3Fields::CALIBRATION,
                                      Gen3Fields::Defaults::CALIBRATION).toInt();
        settings->endGroup();
    }

    void Gen3TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(Gen3Fields::GROUP);
        settings->setValue(Gen3Fields::PRE_TIMER, preTimer);
        settings->setValue(Gen3Fields::TARGET_FRAME, targetFrame);
        settings->setValue(Gen3Fields::CALIBRATION, calibration);
        settings->endGroup();
    }

    int Gen3TimerModel::getPreTimer() const {
        return preTimer;
    }

    void Gen3TimerModel::setPreTimer(int preTimer) {
        if (this->preTimer != preTimer) {
            this->preTimer = preTimer;
            emit preTimerChanged(preTimer);
        }
    }

    int Gen3TimerModel::getTargetFrame() const {
        return targetFrame;
    }

    void Gen3TimerModel::setTargetFrame(int targetFrame) {
        if (this->targetFrame != targetFrame) {
            this->targetFrame = targetFrame;
            emit targetFrameChanged(targetFrame);
        }
    }

    int Gen3TimerModel::getCalibration() const {
        return calibration;
    }

    void Gen3TimerModel::setCalibration(const int calibration) {
        if (this->calibration != calibration) {
            this->calibration = calibration;
            emit calibrationChanged(calibration);
        }
    }

    int Gen3TimerModel::getFrameHit() const {
        return frameHit;
    }

    void Gen3TimerModel::setFrameHit(int frameHit) {
        if (this->frameHit != frameHit) {
            this->frameHit = frameHit;
            emit frameHitChanged(frameHit);
        }
    }
}
