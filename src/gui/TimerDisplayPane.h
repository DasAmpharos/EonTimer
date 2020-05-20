//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERDISPLAYPANE_H
#define EONTIMER_TIMERDISPLAYPANE_H

#include <services/TimerService.h>

#include <QGroupBox>
#include <QLabel>
#include <QtCore/QTimer>

namespace gui {
    class TimerDisplayPane : public QGroupBox {
    public:
        TimerDisplayPane(service::TimerService *timerService,
                         const model::settings::ActionSettingsModel *actionSettings);

    private:
        void initComponents();

        const QString formatTime(const std::chrono::milliseconds &milliseconds) const;

        void updateCurrentStageLbl();

        bool isVisualCueEnabled() const;

    private:
        QLabel *currentStage;
        QLabel *minutesBeforeTarget;
        QLabel *nextStage;
        const model::settings::ActionSettingsModel *actionSettings;
        bool isActive;
        QTimer timer;

    private slots:
        void activate();

        void deactivate();
    };
}  // namespace gui

#endif  // EONTIMER_TIMERDISPLAYPANE_H
