//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERSETTINGS_H
#define EONTIMER_GEN5TIMERSETTINGS_H

#include <QObject>
#include <QSettings>
#include <models/Gen5TimerMode.h>

namespace service::settings {
    class Gen5TimerSettings : public QObject {
    Q_OBJECT
    private:
        QSettings *settings;
    public:
        explicit Gen5TimerSettings(QSettings *settings, QObject *parent = nullptr);

        model::Gen5TimerMode getMode() const;

        void setMode(model::Gen5TimerMode mode);

        int getCalibration() const;

        void setCalibration(int calibration);

        int getTargetDelay() const;

        void setTargetDelay(int targetDelay);

        int getTargetSecond() const;

        void setTargetSecond(int targetSecond);

        int getEntralinkCalibration() const;

        void setEntralinkCalibration(int entralinkCalibration);

        int getFrameCalibration() const;

        void setFrameCalibration(int frameCalibration);

        int getTargetAdvances() const;

        void setTargetAdvances(int targetAdvances);

        // @formatter:off
    signals:
        void modeChanged(model::Gen5TimerMode value);
        void calibrationChanged(int value);
        void targetDelayChanged(int value);
        void targetSecondChanged(int value);
        void entralinkCalibrationChanged(int value);
        void frameCalibrationChanged(int value);
        void targetAdvancesChanged(int value);
        // @formatter:on
    };
}


#endif //EONTIMER_GEN5TIMERSETTINGS_H
