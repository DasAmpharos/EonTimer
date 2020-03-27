//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERSETTINGS_H
#define EONTIMER_GEN3TIMERSETTINGS_H

#include <QSettings>

namespace service::settings {
    class Gen3TimerSettings {
    private:
        QSettings *settings;
    public:
        explicit Gen3TimerSettings(QSettings *settings);

        int getCalibration() const;

        void setCalibration(int calibration);

        int getPreTimer() const;

        void setPreTimer(int preTimer);

        int getTargetFrame() const;

        void setTargetFrame(int targetFrame);
    };
}

#endif //EONTIMER_GEN3TIMERSETTINGS_H
