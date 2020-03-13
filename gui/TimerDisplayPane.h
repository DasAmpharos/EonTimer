//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERDISPLAYPANE_H
#define EONTIMER_TIMERDISPLAYPANE_H

#include <QGroupBox>
#include <QLabel>
#include <services/TimerService.h>

namespace gui {
    class TimerDisplayPane : public QGroupBox {
    public:
        explicit TimerDisplayPane(service::TimerService *timerService);

    private:
        void initComponents();

        const QString formatTime(int milliseconds) const;

    private:
        QLabel *currentStage;
        QLabel *minutesBeforeTarget;
        QLabel *nextStage;
    };
}


#endif //EONTIMER_TIMERDISPLAYPANE_H
