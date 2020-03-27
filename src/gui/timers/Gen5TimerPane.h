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
        QSpinBox *delayHit;
        QSpinBox *secondHit;
        QSpinBox *advancesHit;
    public:
        explicit Gen5TimerPane(service::settings::Gen5TimerSettings *settings,
                               const service::timer::DelayTimer *delayTimer,
                               const service::timer::SecondTimer *secondTimer,
                               const service::timer::EntralinkTimer *entralinkTimer,
                               const service::timer::EnhancedEntralinkTimer *enhancedEntralinkTimer,
                               const service::CalibrationService *calibrationService,
                               QWidget *parent = nullptr);

        std::shared_ptr<std::vector<int>> createStages();

        void calibrate();

    private:
        void initComponents();

        int getDelayCalibration() const;

        int getSecondCalibration() const;

        int getEntralinkCalibration() const;

        int getAdvancesCalibration() const;
    };
}

#endif //EONTIMER_GEN5TIMERPANE_H
