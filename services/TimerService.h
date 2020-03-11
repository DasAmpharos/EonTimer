//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

#include <QObject>

namespace service {
    class TimerService : public QObject {
    Q_OBJECT
    private:
        long currentStage;
        long minutesBeforeTarget;
        long nextStage;

    public:
        void setCurrentStage(long value);

        void setMinutesBeforeTarget(long value);

        void setNextStage(long value);

    signals:

        void currentStageChanged(long value);

        void minutesBeforeTargetChanged(long value);

        void nextStageChanged(long value);
    };
}


#endif //EONTIMER_TIMERSERVICE_H
