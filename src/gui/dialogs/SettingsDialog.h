//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_SETTINGSDIALOG_H
#define EONTIMER_SETTINGSDIALOG_H

#include <QDialog>
#include <models/settings/TimerSettingsModel.h>
#include <gui/settings/TimerSettingsPane.h>
#include <models/settings/ActionSettingsModel.h>
#include <gui/settings/ActionSettingsPane.h>

namespace gui::dialog {
    class SettingsDialog : public QDialog {
    private:
        model::settings::TimerSettingsModel *timerSettings;
        model::settings::ActionSettingsModel *actionSettings;
        settings::TimerSettingsPane *timerSettingsPane;
        settings::ActionSettingsPane *actionSettingsPane;
    public:
        explicit SettingsDialog(model::settings::TimerSettingsModel *timerSettings,
                                model::settings::ActionSettingsModel *actionSettings,
                                QWidget *parent = nullptr);

    private:
        void initComponents();
    };
}


#endif //EONTIMER_SETTINGSDIALOG_H
