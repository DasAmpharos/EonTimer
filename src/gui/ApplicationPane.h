//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_APPLICATIONPANE_H
#define EONTIMER_APPLICATIONPANE_H

#include <QWidget>
#include <QSettings>
#include <models/settings/ActionSettingsModel.h>
#include <models/settings/TimerSettingsModel.h>
#include <services/TimerService.h>
#include "TimerDisplayPane.h"
#include "timers/Gen5TimerPane.h"
#include "timers/Gen4TimerPane.h"
#include "timers/Gen3TimerPane.h"
#include "timers/CustomTimerPane.h"

namespace gui {
    class ApplicationPane : public QWidget {
    Q_OBJECT
    private:
        QSettings *settings;
        model::settings::ActionSettingsModel *actionSettings;
        model::settings::TimerSettingsModel *timerSettings;
        service::TimerService *timerService;
        TimerDisplayPane *timerDisplayPane;
        timer::Gen5TimerPane *gen5TimerPane;
        timer::Gen4TimerPane *gen4TimerPane;
        timer::Gen3TimerPane *gen3TimerPane;
        timer::CustomTimerPane *customTimerPane;
    public:
        ApplicationPane(QSettings *settings,
                        model::settings::ActionSettingsModel *actionSettings,
                        model::settings::TimerSettingsModel *timerSettings,
                        service::TimerService *timerService,
                        QWidget *parent = nullptr);

    private:
        void initComponents();

        uint getSelectedTab() const;

        void setSelectedTab(uint timerType);

        void updateTimer();

        // @formatter:off
    private slots:
        void onUpdate();
        // @formatter:on
    };
}


#endif //EONTIMER_APPLICATIONPANE_H
