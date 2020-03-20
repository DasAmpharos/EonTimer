//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERSETTINGS_H
#define EONTIMER_GEN5TIMERSETTINGS_H

#include <QSettings>

namespace service::settings {
    class Gen5TimerSettings {
    private:
        QSettings *settings;
    public:
        explicit Gen5TimerSettings(QSettings *settings);

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
    };
}


#endif //EONTIMER_GEN5TIMERSETTINGS_H
