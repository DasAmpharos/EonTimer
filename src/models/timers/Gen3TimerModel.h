//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERMODEL_H
#define EONTIMER_GEN3TIMERMODEL_H

#include <QObject>
#include <QSettings>

namespace model::timer {
    class Gen3TimerModel : public QObject {
    Q_OBJECT
    private:
        int preTimer;
        int targetFrame;
        int calibration;
        int frameHit;
    public:
        explicit Gen3TimerModel(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        int getPreTimer() const;

        void setPreTimer(int preTimer);

        int getTargetFrame() const;

        void setTargetFrame(int targetFrame);

        int getCalibration() const;

        void setCalibration(int calibration);

        int getFrameHit() const;

        void setFrameHit(int frameHit);

        // @formatter:off
    signals:
        void preTimerChanged(int value);
        void targetFrameChanged(int value);
        void calibrationChanged(int value);
        // @formatter:on
    };
}

#endif //EONTIMER_GEN3TIMERMODEL_H
