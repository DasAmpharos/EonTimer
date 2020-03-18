//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_TIMERSETTINGSPANE_H
#define EONTIMER_TIMERSETTINGSPANE_H

#include <QWidget>
#include <services/settings/TimerSettings.h>
#include <QComboBox>
#include <QSpinBox>
#include <QCheckBox>

namespace gui::settings {
    class TimerSettingsPane : public QWidget {
    private:
        service::settings::TimerSettings *settings;
        QComboBox *console;
        QCheckBox *precisionCalibrationEnabled;
        QSpinBox *refreshInterval;
    public:
        TimerSettingsPane(service::settings::TimerSettings *settings,
                          QWidget *parent = nullptr);

        void updateSettings();
    private:
        void initComponents();
    };
}

#endif //EONTIMER_TIMERSETTINGSPANE_H
