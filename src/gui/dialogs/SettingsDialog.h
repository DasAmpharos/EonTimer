//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_SETTINGSDIALOG_H
#define EONTIMER_SETTINGSDIALOG_H

#include <QDialog>
#include <services/settings/TimerSettings.h>
#include <gui/settings/TimerSettingsPane.h>
#include <services/settings/ActionSettings.h>
#include <gui/settings/ActionSettingsPane.h>

namespace gui::dialog {
    class SettingsDialog : public QDialog {
    private:
        service::settings::TimerSettings *timerSettings;
        settings::TimerSettingsPane *timerSettingsPane;
        service::settings::ActionSettings *actionSettings;
        settings::ActionSettingsPane *actionSettingsPane;
    public:
        explicit SettingsDialog(service::settings::TimerSettings *timerSettings,
                                service::settings::ActionSettings *actionSettings,
                                QWidget *parent = nullptr);

    private:
        void initComponents();
    };
}


#endif //EONTIMER_SETTINGSDIALOG_H
