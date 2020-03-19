//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERPANE_H
#define EONTIMER_GEN5TIMERPANE_H

#include <QWidget>
#include <QComboBox>
#include <services/settings/Gen5TimerSettings.h>
#include <services/CalibrationService.h>
#include <QSpinBox>

namespace gui::timer {
    class Gen5TimerPane : public QWidget {
    Q_OBJECT
    private:
        service::settings::Gen5TimerSettings *settings;
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
                               const service::CalibrationService *calibrationService,
                               QWidget *parent = nullptr);

    private:
        void initComponents();
    };
}

#endif //EONTIMER_GEN5TIMERPANE_H
