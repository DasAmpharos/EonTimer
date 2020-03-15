//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_APPLICATIONPANE_H
#define EONTIMER_APPLICATIONPANE_H

#include <QWidget>
#include <services/TimerService.h>

namespace gui {
    // @formatter:off
    class Gen4TimerPane;
    class TimerDisplayPane;
    // @formatter:on

    class ApplicationPane : public QWidget {
    Q_OBJECT
    private:
        Gen4TimerPane *gen4TimerPane;
        TimerDisplayPane *timerDisplayPane;
        service::TimerService *timerService;
    public:
        explicit ApplicationPane(QWidget *parent = nullptr);

    private:
        void initComponents();

        // @formatter:off
    private slots:
        void onUpdate();
        // @formatter:on
    };
}


#endif //EONTIMER_APPLICATIONPANE_H
