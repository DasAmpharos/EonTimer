//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERDISPLAYPANE_H
#define EONTIMER_TIMERDISPLAYPANE_H

#include <QGroupBox>
#include <QLabel>
#include "../services/TimerService.h"

namespace ui {
    class TimerDisplayPane : public QGroupBox {
    public:
        explicit TimerDisplayPane(service::TimerService *timerService);
    private:
        void initComponents();

    private:
        QLabel *currentStage;
        QLabel *minutesBeforeTarget;
        QLabel *nextStage;
    };
}


#endif //EONTIMER_TIMERDISPLAYPANE_H
