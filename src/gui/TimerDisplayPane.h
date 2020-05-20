//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERDISPLAYPANE_H
#define EONTIMER_TIMERDISPLAYPANE_H

#include <services/TimerService.h>

#include <QGroupBox>
#include <QLabel>

namespace gui {
    class TimerDisplayPane : public QGroupBox {
    public:
        explicit TimerDisplayPane(service::TimerService *timerService);

    private:
        void initComponents();

        const QString formatTime(const std::chrono::milliseconds &milliseconds) const;

    private:
        QLabel *currentStage;
        QLabel *minutesBeforeTarget;
        QLabel *nextStage;
    };
}  // namespace gui

#endif  // EONTIMER_TIMERDISPLAYPANE_H
