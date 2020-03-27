//
// Created by Dylan Meadows on 2020-03-17.
//

#ifndef EONTIMER_GEN4TIMERSETTINGS_H
#define EONTIMER_GEN4TIMERSETTINGS_H

#include <QObject>
#include <QSettings>

namespace service::settings {
    class Gen4TimerSettings : public QObject {
    Q_OBJECT
    private:
        QSettings *settings;
    public:
        explicit Gen4TimerSettings(QSettings *settings, QObject *parent = nullptr);

        int getCalibratedDelay() const;

        void setCalibratedDelay(int calibratedDelay);

        int getCalibratedSecond() const;

        void setCalibratedSecond(int calibratedSecond);

        int getTargetDelay() const;

        void setTargetDelay(int targetDelay);

        int getTargetSecond() const;

        void setTargetSecond(int targetSecond);

        // @formatter:off
    signals:
        void calibratedDelayChanged(int calibratedDelay);
        void calibratedSecondChanged(int calibratedSecond);
        void targetDelayChanged(int targetDelay);
        void targetSecondChanged(int targetSecond);
        // @formatter:on
    };
}

#endif //EONTIMER_GEN4TIMERSETTINGS_H
