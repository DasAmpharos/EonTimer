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
#include <services/timers/EnhancedEntralinkTimer.h>
#include <services/CalibrationService.h>
#include <services/TimerService.h>
#include <QGridLayout>
#include <gui/util/FieldSet.h>
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
        const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer;
        const service::CalibrationService *calibrationService;
        service::TimerService *timerService;

        QGridLayout *timerForm;
        QGridLayout *calibrationForm;
        util::FieldSet<QComboBox> mode;
        util::FieldSet<QSpinBox> targetDelay;
        util::FieldSet<QSpinBox> targetSecond;
        util::FieldSet<QSpinBox> targetAdvances;
        util::FieldSet<QSpinBox> calibration;
        util::FieldSet<QSpinBox> entralinkCalibration;
        util::FieldSet<QSpinBox> frameCalibration;
        util::FieldSet<QSpinBox> delayHit;
        util::FieldSet<QSpinBox> secondHit;
        util::FieldSet<QSpinBox> advancesHit;
    public:
        explicit Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                               const service::timer::DelayTimer *delayTimer,
                               const service::timer::SecondTimer *secondTimer,
                               const service::timer::EntralinkTimer *entralinkTimer,
                               const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                               const service::CalibrationService *calibrationService,
                               service::TimerService *timerService,
                               QWidget *parent = nullptr);

        void calibrateTimer();

        void updateTimer();

    private:
        void initComponents();

        void updateComponents();

        int getDelayCalibration() const;

        int getSecondCalibration() const;

        int getEntralinkCalibration() const;
    };
}

#endif //EONTIMER_GEN5TIMERPANE_H
