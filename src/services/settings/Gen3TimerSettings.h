//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERSETTINGS_H
#define EONTIMER_GEN3TIMERSETTINGS_H

#include <QObject>
#include <QSettings>

namespace service::settings {
    class Gen3TimerSettings : public QObject {
    Q_OBJECT
    private:
        QSettings *settings;
    public:
        explicit Gen3TimerSettings(QSettings *settings, QObject *parent = nullptr);

        int getPreTimer() const;

        void setPreTimer(int preTimer);

        int getTargetFrame() const;

        void setTargetFrame(int targetFrame);

        int getCalibration() const;

        void setCalibration(int calibration);

        // @formatter:off
    signals:
        void preTimerChanged(int value);
        void targetFrameChanged(int value);
        void calibrationChanged(int value);
        // @formatter:on
    };
}

#endif //EONTIMER_GEN3TIMERSETTINGS_H
