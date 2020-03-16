//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_SETTINGSDIALOG_H
#define EONTIMER_SETTINGSDIALOG_H

#include <QDialog>
#include <services/settings/TimerSettings.h>
#include <services/settings/ActionSettings.h>

namespace gui::dialog {
    class SettingsDialog : public QDialog {
    private:
        service::settings::TimerSettings *timerSettings;
        service::settings::ActionSettings *actionSettings;
    public:
        explicit SettingsDialog(service::settings::TimerSettings *timerSettings,
                                service::settings::ActionSettings *actionSettings,
                                QWidget *parent = nullptr);

    private:
        void initComponents();
    };
}


#endif //EONTIMER_SETTINGSDIALOG_H
