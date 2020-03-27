//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_GEN4TIMERPANE_H
#define EONTIMER_GEN4TIMERPANE_H

#include <QWidget>
#include <services/TimerService.h>
#include <services/CalibrationService.h>
#include <services/timers/DelayTimer.h>
#include <services/settings/Gen4TimerSettings.h>

class QSpinBox;

namespace gui::timer {
    class Gen4TimerPane : public QWidget {
    Q_OBJECT
    private:
        service::settings::Gen4TimerSettings *settings;
        const service::timer::DelayTimer *delayTimer;
        const service::CalibrationService *calibrationService;
        service::TimerService *timerService;
        QSpinBox *targetDelay;
        QSpinBox *targetSecond;
        QSpinBox *calibratedDelay;
        QSpinBox *calibratedSecond;
        QSpinBox *delayHit;
    public:
        explicit Gen4TimerPane(service::settings::Gen4TimerSettings *settings,
                               const service::timer::DelayTimer *delayTimer,
                               const service::CalibrationService *calibrationService,
                               service::TimerService *timerService,
                               QWidget *parent = nullptr);

        void calibrateTimer();

        void updateTimer();

    private:
        void initComponents();
    };
}


#endif //EONTIMER_GEN4TIMERPANE_H
