//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_TIMERSETTINGSPANE_H
#define EONTIMER_TIMERSETTINGSPANE_H

#include <models/settings/TimerSettingsModel.h>

#include <QCheckBox>
#include <QComboBox>
#include <QSpinBox>
#include <QWidget>

namespace gui::settings {
    class TimerSettingsPane : public QWidget {
    private:
        model::settings::TimerSettingsModel *model;
        QComboBox *console;
        QCheckBox *precisionCalibrationEnabled;
        QSpinBox *refreshInterval;

    public:
        TimerSettingsPane(model::settings::TimerSettingsModel *model, QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();
    };
}  // namespace gui::settings

#endif  // EONTIMER_TIMERSETTINGSPANE_H
