//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_APPLICATIONPANE_H
#define EONTIMER_APPLICATIONPANE_H

#include <QWidget>
#include "TimerDisplayPane.h"
#include "timers/Gen4TimerPane.h"
#include <services/TimerService.h>

namespace gui {
    class ApplicationPane : public QWidget {
    Q_OBJECT
    private:
        service::settings::ActionSettings *actionSettings;
        service::settings::TimerSettings *timerSettings;
        service::TimerService *timerService;
        TimerDisplayPane *timerDisplayPane;
        timer::Gen4TimerPane *gen4TimerPane;
    public:
        ApplicationPane(service::settings::ActionSettings *actionSettings,
                        service::settings::TimerSettings *timerSettings,
                        service::TimerService *timerService,
                        QWidget *parent = nullptr);

    private:
        void initComponents();

        // @formatter:off
    private slots:
        void onUpdate();
        // @formatter:on
    };
}


#endif //EONTIMER_APPLICATIONPANE_H
