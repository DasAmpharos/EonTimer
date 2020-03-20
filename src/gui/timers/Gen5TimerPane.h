//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERPANE_H
#define EONTIMER_GEN5TIMERPANE_H

#include <QWidget>
#include <services/settings/Gen5TimerSettings.h>
#include <services/timers/DelayTimer.h>
#include <services/timers/SecondTimer.h>
#include <services/timers/EntralinkTimer.h>
#include <services/CalibrationService.h>
#include <QComboBox>
#include <QSpinBox>

namespace gui::timer {
    class Gen5TimerPane : public QWidget {
    Q_OBJECT
    private:
        service::settings::Gen5TimerSettings *settings;
        const service::timer::DelayTimer *delayTimer;
        const service::timer::SecondTimer *secondTimer;
        const service::timer::EntralinkTimer *entralinkTimer;
        const service::CalibrationService *calibrationService;
        QComboBox *modeField;
        QSpinBox *calibrationField;
        QSpinBox *targetDelayField;
        QSpinBox *targetSecondField;
        QSpinBox *entralinkCalibrationField;
        QSpinBox *frameCalibrationField;
        QSpinBox *targetAdvancesField;
        QSpinBox *delayHitField;
        QSpinBox *secondHitField;
        QSpinBox *actualAdvancesField;
    public:
        explicit Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                               const service::timer::DelayTimer *delayTimer,
                               const service::timer::SecondTimer *secondTimer,
                               const service::timer::EntralinkTimer *entralinkTimer,
                               const service::CalibrationService *calibrationService,
                               QWidget *parent = nullptr);

        void calibrateTimer();

    private:
        void initComponents();

        void updateTimer();
    };
}

#endif //EONTIMER_GEN5TIMERPANE_H
