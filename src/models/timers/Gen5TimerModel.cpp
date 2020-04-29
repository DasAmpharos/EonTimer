//
// Created by Dylan Meadows on 2020-03-19.
//

#include "Gen5TimerModel.h"

namespace model::timer {
    namespace Gen5Fields {
        const char *GROUP = "gen5";
        const char *MODE = "mode";
        const char *CALIBRATION = "calibration";
        const char *FRAME_CALIBRATION = "frameCalibration";
        const char *ENTRALINK_CALIBRATION = "entralinkCalibration";
        const char *TARGET_DELAY = "targetDelay";
        const char *TARGET_SECOND = "targetSecond";
        const char *TARGET_ADVANCES = "targetAdvances";

        namespace Defaults {
            const int MODE = 0;
            const int CALIBRATION = 95;
            const int FRAME_CALIBRATION = 0;
            const int ENTRALINK_CALIBRATION = 256;
            const int TARGET_DELAY = 1200;
            const int TARGET_SECOND = 50;
            const int TARGET_ADVANCES = 100;
        }  // namespace Defaults
    }      // namespace Gen5Fields

    Gen5TimerModel::Gen5TimerModel(QSettings *settings, QObject *parent)
        : QObject(parent) {
        settings->beginGroup(Gen5Fields::GROUP);
        mode = model::gen5TimerMode(
            settings->value(Gen5Fields::MODE, Gen5Fields::Defaults::MODE)
                .toInt());
        calibration = settings
                          ->value(Gen5Fields::CALIBRATION,
                                  Gen5Fields::Defaults::CALIBRATION)
                          .toInt();
        frameCalibration = settings
                               ->value(Gen5Fields::FRAME_CALIBRATION,
                                       Gen5Fields::Defaults::FRAME_CALIBRATION)
                               .toInt();
        entralinkCalibration =
            settings
                ->value(Gen5Fields::ENTRALINK_CALIBRATION,
                        Gen5Fields::Defaults::ENTRALINK_CALIBRATION)
                .toInt();
        targetDelay = settings
                          ->value(Gen5Fields::TARGET_DELAY,
                                  Gen5Fields::Defaults::TARGET_DELAY)
                          .toInt();
        targetSecond = settings
                           ->value(Gen5Fields::TARGET_SECOND,
                                   Gen5Fields::Defaults::TARGET_SECOND)
                           .toInt();
        targetAdvances = settings
                             ->value(Gen5Fields::TARGET_ADVANCES,
                                     Gen5Fields::Defaults::TARGET_ADVANCES)
                             .toInt();
        settings->endGroup();
    }

    void Gen5TimerModel::sync(QSettings *settings) const {
        settings->beginGroup(Gen5Fields::GROUP);
        settings->setValue(Gen5Fields::MODE, model::indexOf(mode));
        settings->setValue(Gen5Fields::CALIBRATION, calibration);
        settings->setValue(Gen5Fields::FRAME_CALIBRATION, frameCalibration);
        settings->setValue(Gen5Fields::ENTRALINK_CALIBRATION,
                           entralinkCalibration);
        settings->setValue(Gen5Fields::TARGET_DELAY, targetDelay);
        settings->setValue(Gen5Fields::TARGET_SECOND, targetSecond);
        settings->setValue(Gen5Fields::TARGET_ADVANCES, targetAdvances);
        settings->endGroup();
    }

    model::Gen5TimerMode Gen5TimerModel::getMode() const { return mode; }

    void Gen5TimerModel::setMode(model::Gen5TimerMode mode) {
        if (this->mode != mode) {
            this->mode = mode;
            emit modeChanged(mode);
        }
    }

    int Gen5TimerModel::getCalibration() const { return calibration; }

    void Gen5TimerModel::setCalibration(int calibration) {
        if (this->calibration != calibration) {
            this->calibration = calibration;
            emit calibrationChanged(calibration);
        }
    }

    int Gen5TimerModel::getFrameCalibration() const { return frameCalibration; }

    void Gen5TimerModel::setFrameCalibration(int frameCalibration) {
        if (this->frameCalibration != frameCalibration) {
            this->frameCalibration = frameCalibration;
            emit frameCalibrationChanged(frameCalibration);
        }
    }

    int Gen5TimerModel::getEntralinkCalibration() const {
        return entralinkCalibration;
    }

    void Gen5TimerModel::setEntralinkCalibration(int entralinkCalibration) {
        if (this->entralinkCalibration != entralinkCalibration) {
            this->entralinkCalibration = entralinkCalibration;
            emit entralinkCalibrationChanged(entralinkCalibration);
        }
    }

    int Gen5TimerModel::getTargetDelay() const { return targetDelay; }

    void Gen5TimerModel::setTargetDelay(int targetDelay) {
        if (this->targetDelay != targetDelay) {
            this->targetDelay = targetDelay;
            emit targetDelayChanged(targetDelay);
        }
    }

    int Gen5TimerModel::getTargetSecond() const { return targetSecond; }

    void Gen5TimerModel::setTargetSecond(int targetSecond) {
        if (this->targetSecond != targetSecond) {
            this->targetSecond = targetSecond;
            emit targetSecondChanged(targetSecond);
        }
    }

    int Gen5TimerModel::getTargetAdvances() const { return targetAdvances; }

    void Gen5TimerModel::setTargetAdvances(int targetAdvances) {
        if (this->targetAdvances != targetAdvances) {
            this->targetAdvances = targetAdvances;
            emit targetAdvancesChanged(targetAdvances);
        }
    }

    int Gen5TimerModel::getDelayHit() const { return delayHit; }

    void Gen5TimerModel::setDelayHit(const int delayHit) {
        if (this->delayHit != delayHit) {
            this->delayHit = delayHit;
            emit delayHitChanged(delayHit);
        }
    }

    int Gen5TimerModel::getSecondHit() const { return secondHit; }

    void Gen5TimerModel::setSecondHit(const int secondHit) {
        if (this->secondHit != secondHit) {
            this->secondHit = secondHit;
            emit secondHitChanged(secondHit);
        }
    }

    int Gen5TimerModel::getAdvancesHit() const { return advancesHit; }

    void Gen5TimerModel::setAdvancesHit(const int advancesHit) {
        if (this->advancesHit != advancesHit) {
            this->advancesHit = advancesHit;
            emit advancesHitChanged(advancesHit);
        }
    }
}  // namespace model::timer