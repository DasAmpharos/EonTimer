//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_APPLICATIONPANE_H
#define EONTIMER_APPLICATIONPANE_H

#include <QWidget>

namespace service {
    // @formatter:off
    class SettingsService;
    class CalibrationService;
    // @formatter:on
}

namespace ui {
    // @formatter:off
    class Gen5TimerPane;
    class Gen4TimerPane;
    class Gen3TimerPane;
    class CustomTimerPane;
    class TimerDisplayPane;
    // @formatter:on

    class ApplicationPane : public QWidget {
    Q_OBJECT
    private:
        Gen5TimerPane *gen5TimerPane;
        Gen4TimerPane *gen4TimerPane;
        Gen3TimerPane *gen3TimerPane;
        CustomTimerPane *customTimerPane;
        TimerDisplayPane *timerDisplayPane;
    public:
        explicit ApplicationPane(QWidget *parent = nullptr);

    private:
        void initComponents();
    };
}


#endif //EONTIMER_APPLICATIONPANE_H
