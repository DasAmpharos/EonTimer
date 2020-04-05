//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERMODEL_H
#define EONTIMER_GEN5TIMERMODEL_H

#include <models/Gen5TimerMode.h>

#include <QObject>
#include <QSettings>

namespace model::timer {
    class Gen5TimerModel : public QObject {
        Q_OBJECT
    private:
        model::Gen5TimerMode mode;
        int calibration;
        int frameCalibration;
        int entralinkCalibration;
        int targetDelay;
        int targetSecond;
        int targetAdvances;
        int delayHit;
        int secondHit;
        int advancesHit;

    public:
        explicit Gen5TimerModel(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        model::Gen5TimerMode getMode() const;

        void setMode(model::Gen5TimerMode mode);

        int getCalibration() const;

        void setCalibration(int calibration);

        int getFrameCalibration() const;

        void setFrameCalibration(int frameCalibration);

        int getEntralinkCalibration() const;

        void setEntralinkCalibration(int entralinkCalibration);

        int getTargetDelay() const;

        void setTargetDelay(int targetDelay);

        int getTargetSecond() const;

        void setTargetSecond(int targetSecond);

        int getTargetAdvances() const;

        void setTargetAdvances(int targetAdvances);

        int getDelayHit() const;

        void setDelayHit(int delayHit);

        int getSecondHit() const;

        void setSecondHit(int secondHit);

        int getAdvancesHit() const;

        void setAdvancesHit(int advancesHit);

        // @formatter:off
    signals:
        void modeChanged(model::Gen5TimerMode value);
        void calibrationChanged(int value);
        void frameCalibrationChanged(int value);
        void entralinkCalibrationChanged(int value);
        void targetDelayChanged(int value);
        void targetSecondChanged(int value);
        void targetAdvancesChanged(int value);
        // @formatter:on
    };
}  // namespace model::timer

#endif  // EONTIMER_GEN5TIMERMODEL_H
