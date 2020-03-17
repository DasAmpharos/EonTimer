//
// Created by Dylan Meadows on 2020-03-17.
//

#ifndef EONTIMER_GEN4TIMERSETTINGS_H
#define EONTIMER_GEN4TIMERSETTINGS_H

#include <QSettings>

namespace service::settings {
    class Gen4TimerSettings {
    private:
        QSettings *settings;
    public:
        explicit Gen4TimerSettings(QSettings *settings);

        int getCalibratedDelay() const;

        void setCalibratedDelay(int calibratedDelay);

        int getCalibratedSecond() const;

        void setCalibratedSecond(int calibratedSecond);

        int getTargetDelay() const;

        void setTargetDelay(int targetDelay);

        int getTargetSecond() const;

        void setTargetSecond(int targetSecond);
    };
}

#endif //EONTIMER_GEN4TIMERSETTINGS_H
