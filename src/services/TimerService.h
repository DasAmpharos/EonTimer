//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSERVICE_H
#define EONTIMER_TIMERSERVICE_H

#include <models/TimerState.h>
#include <models/settings/ActionSettingsModel.h>
#include <models/settings/TimerSettingsModel.h>

#include <QObject>
#include <QThread>
#include <memory>
#include <vector>
#include <thread>

namespace service {
    class TimerService : public QObject {
        Q_OBJECT
    private:
        bool running;
        QThread *timerThread;
        std::shared_ptr<std::vector<int>> stages;
        model::settings::TimerSettingsModel *timerSettings;
        model::settings::ActionSettingsModel *actionSettings;

    public:
        explicit TimerService(model::settings::TimerSettingsModel *timerSettings,
                              model::settings::ActionSettingsModel *actionSettings,
                              QObject *parent = nullptr);

        ~TimerService() override;

        void setStages(std::shared_ptr<std::vector<int>> stages);

        void start();

        void stop();

        bool isRunning() const;

    private:
        void reset();

        void run();

        std::chrono::microseconds runStage(std::chrono::microseconds stage, std::chrono::microseconds elapsed);

        // @formatter:off
    signals:
        void activated(bool);
        void actionTriggered();
        void stateChanged(const model::TimerState &state);
        void minutesBeforeTargetChanged(const std::chrono::minutes &minutesBeforeTarget);
        void nextStageChanged(const std::chrono::milliseconds &nextStage);
        // @formatter:on
    };
}  // namespace service

#endif  // EONTIMER_TIMERSERVICE_H
