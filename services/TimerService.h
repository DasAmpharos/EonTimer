//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

#include <QObject>
#include <vector>
#include "TimerThread.h"
#include "SoundService.h"
#include <models/TimerState.h>

namespace service {
    class TimerService : public QObject {
    Q_OBJECT
    private:
        bool running;
        TimerThread *timerThread;
        std::shared_ptr<std::vector<int>> stages;
        SoundService *sounds;

    public:
        explicit TimerService(SoundService *sounds, QObject *parent = nullptr);

        ~TimerService() override;

        void start();

        void stop();

        void setStages(std::shared_ptr<std::vector<int>> stages);

        bool isRunning() const;

    private:
        void run();

        std::chrono::milliseconds runStage(uint8_t stageIndex, std::chrono::milliseconds elapsed);

        void reset();

        void publishStateChange(const std::chrono::milliseconds &currentStage,
                                const std::chrono::milliseconds &elapsed);

        // @formatter:off
    signals:
        void stateChanged(const model::TimerState &state);
        void minutesBeforeTargetChanged(const std::chrono::minutes &minutesBeforeTarget);
        void nextStageChanged(const std::chrono::milliseconds &nextStage);
        void actionTriggered();
        // @formatter:on
    };
}


#endif //EONTIMER_TIMERSERVICE_H
