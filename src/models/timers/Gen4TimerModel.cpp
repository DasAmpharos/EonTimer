//
// Created by Dylan Meadows on 2020-03-17.
//

#include <QtCore>
#include "Gen4TimerModel.h"

namespace model::timer {
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

    Gen4TimerModel::Gen4TimerModel(QSettings *settings, QObject *parent)
        : QObject(parent) {
        calibratedDelay = settings->value(Gen4Fields::CALIBRATED_DELAY,
                                          Gen4Fields::Defaults::CALIBRATED_DELAY).toInt();
        calibratedSecond = settings->value(Gen4Fields::CALIBRATED_SECOND,
                                           Gen4Fields::Defaults::CALIBRATED_SECOND).toInt();
        targetDelay = settings->value(Gen4Fields::TARGET_DELAY,
                                      Gen4Fields::Defaults::TARGET_DELAY).toInt();
        targetSecond = settings->value(Gen4Fields::TARGET_SECOND,
                                       Gen4Fields::Defaults::TARGET_SECOND).toInt();
    }

    void Gen4TimerModel::sync(QSettings *settings) const {
        settings->setValue(Gen4Fields::CALIBRATED_DELAY, calibratedDelay);
        settings->setValue(Gen4Fields::CALIBRATED_SECOND, calibratedSecond);
        settings->setValue(Gen4Fields::TARGET_DELAY, targetDelay);
        settings->setValue(Gen4Fields::TARGET_SECOND, targetSecond);
    }

    int Gen4TimerModel::getCalibratedDelay() const {
        return calibratedDelay;
    }

    void Gen4TimerModel::setCalibratedDelay(const int calibratedDelay) {
        if (this->calibratedDelay != calibratedDelay) {
            this->calibratedDelay = calibratedDelay;
            emit calibratedDelayChanged(calibratedDelay);
        }
    }

    int Gen4TimerModel::getCalibratedSecond() const {
        return calibratedSecond;
    }

    void Gen4TimerModel::setCalibratedSecond(const int calibratedSecond) {
        if (this->calibratedSecond != calibratedSecond) {
            this->calibratedSecond = calibratedSecond;
            emit calibratedSecondChanged(calibratedSecond);
        }
    }

    int Gen4TimerModel::getTargetDelay() const {
        return targetDelay;
    }

    void Gen4TimerModel::setTargetDelay(const int targetDelay) {
        if (this->targetDelay != targetDelay) {
            this->targetDelay = targetDelay;
            emit targetDelayChanged(targetDelay);
        }
    }

    int Gen4TimerModel::getTargetSecond() const {
        return targetSecond;
    }

    void Gen4TimerModel::setTargetSecond(const int targetSecond) {
        if (this->targetSecond != targetSecond) {
            this->targetSecond = targetSecond;
            emit targetSecondChanged(targetSecond);
        }
    }

    int Gen4TimerModel::getDelayHit() const {
        return delayHit;
    }

    void Gen4TimerModel::setDelayHit(int delayHit) {
        this->delayHit = delayHit;
    }
}
