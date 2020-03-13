//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

#include <QObject>
#include <vector>

namespace model {
    struct TimerState;
}

namespace service {
    class TimerService : public QObject {
    Q_OBJECT
    private:
        bool running;
        std::shared_ptr<std::vector<int>> stages;
        uint8_t currentStageIdx;
        int elapsed;

    public:
        void start();

        void stop();

        void setStages(std::shared_ptr<std::vector<int>> stages);

        const bool isRunning() const;

    private:
        void reset();

        void publishStateChange();

        // @formatter:off
    signals:
        void stateChanged(const model::TimerState &state);
        void minutesBeforeTargetChanged(int value);
        void nextStageChanged(int value);
        // @formatter:on
    };
}


#endif //EONTIMER_TIMERSERVICE_H
