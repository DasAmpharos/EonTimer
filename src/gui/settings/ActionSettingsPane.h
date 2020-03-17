//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_ACTIONSETTINGSPANE_H
#define EONTIMER_ACTIONSETTINGSPANE_H

#include <QWidget>
#include <services/settings/ActionSettings.h>
#include <QSpinBox>
#include <QComboBox>

namespace gui::settings {
    class ActionSettingsPane : public QWidget {
    Q_OBJECT
    private:
        service::settings::ActionSettings *settings;
        QComboBox *sound;
        QSpinBox *interval;
        QSpinBox *count;
    public:
        ActionSettingsPane(service::settings::ActionSettings *settings,
                           QWidget *parent = nullptr);

        void updateSettings();

    private:
        void initComponents();
    };
}

#endif //EONTIMER_ACTIONSETTINGSPANE_H
