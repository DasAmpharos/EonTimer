//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_ACTIONSETTINGSPANE_H
#define EONTIMER_ACTIONSETTINGSPANE_H

#include <models/settings/ActionSettingsModel.h>

#include <QComboBox>
#include <QSpinBox>
#include <QWidget>

namespace gui::settings {
    class ActionSettingsPane : public QWidget {
        Q_OBJECT
    private:
        model::settings::ActionSettingsModel *settings;
        QComboBox *sound;
        QSpinBox *interval;
        QSpinBox *count;

    public:
        explicit ActionSettingsPane(
            model::settings::ActionSettingsModel *settings,
            QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();
    };
}  // namespace gui::settings

#endif  // EONTIMER_ACTIONSETTINGSPANE_H
