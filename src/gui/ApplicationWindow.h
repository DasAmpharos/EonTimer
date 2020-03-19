//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_APPLICATIONWINDOW_H
#define EONTIMER_APPLICATIONWINDOW_H

#include <QMainWindow>
#include <QSettings>
#include <services/settings/ActionSettings.h>
#include <services/settings/TimerSettings.h>
#include <services/TimerService.h>
#include "ApplicationPane.h"

namespace gui {
    class ApplicationWindow : public QMainWindow {
    Q_OBJECT
    private:
        QSettings *settings;
        service::settings::ActionSettings *actionSettings;
        service::settings::TimerSettings *timerSettings;
        service::TimerService *timerService;
        ApplicationPane *applicationPane;
    public:
        explicit ApplicationWindow(QWidget *parent = nullptr);

    private:
        void initComponents();

    protected:
        void closeEvent(QCloseEvent *event) override;

        // @formatter:off
    private slots:
        void onPreferencesTriggered();
        // @formatter:on
    };
}


#endif //EONTIMER_APPLICATIONWINDOW_H
