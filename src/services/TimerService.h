//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

#include <QObject>
#include <QThread>
#include <memory>
#include <vector>
#include <services/settings/TimerSettings.h>
#include <services/settings/ActionSettings.h>
#include <models/TimerState.h>

namespace service {
    class TimerService : public QObject {
    Q_OBJECT
    private:
        bool running;
        QThread *timerThread;
        std::shared_ptr<std::vector<int>> stages;
        settings::TimerSettings *timerSettings;
        settings::ActionSettings *actionSettings;

    public:
        explicit TimerService(settings::TimerSettings *timerSettings,
                              settings::ActionSettings *actionSettings,
                              QObject *parent = nullptr);

        ~TimerService() override;

        void setStages(std::shared_ptr<std::vector<int>> stages);

        void start();

        void stop();

        bool isRunning() const;

    private:
        void reset();

        void run();

        std::chrono::milliseconds runStage(uint8_t stageIndex, std::chrono::milliseconds elapsed);

        // @formatter:off
    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const model::TimerState &state);
        void minutesBeforeTargetChanged(const std::chrono::minutes &minutesBeforeTarget);
        void nextStageChanged(const std::chrono::milliseconds &nextStage);
        // @formatter:on
    };
}

#endif //EONTIMER_TIMERSERVICE_H
