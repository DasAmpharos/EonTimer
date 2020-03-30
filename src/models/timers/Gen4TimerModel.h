//
// Created by Dylan Meadows on 2020-03-17.
//

#ifndef EONTIMER_GEN4TIMERMODEL_H
#define EONTIMER_GEN4TIMERMODEL_H

#include <QObject>
#include <QSettings>

namespace model::timer {
    class Gen4TimerModel : public QObject {
    Q_OBJECT
    private:
        int calibratedDelay;
        int calibratedSecond;
        int targetDelay;
        int targetSecond;
        int delayHit;
    public:
        explicit Gen4TimerModel(QSettings *settings, QObject *parent = nullptr);

        void sync(QSettings *settings) const;

        int getCalibratedDelay() const;

        void setCalibratedDelay(int calibratedDelay);

        int getCalibratedSecond() const;

        void setCalibratedSecond(int calibratedSecond);

        int getTargetDelay() const;

        void setTargetDelay(int targetDelay);

        int getTargetSecond() const;

        void setTargetSecond(int targetSecond);

        int getDelayHit() const;

        void setDelayHit(int delayHit);

        // @formatter:off
    signals:
        void calibratedDelayChanged(int calibratedDelay);
        void calibratedSecondChanged(int calibratedSecond);
        void targetDelayChanged(int targetDelay);
        void targetSecondChanged(int targetSecond);
        // @formatter:on
    };
}

#endif //EONTIMER_GEN4TIMERMODEL_H
