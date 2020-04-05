//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_GEN4TIMERPANE_H
#define EONTIMER_GEN4TIMERPANE_H

#include <models/timers/Gen4TimerModel.h>
#include <services/CalibrationService.h>
#include <services/timers/DelayTimer.h>

#include <QSpinBox>
#include <QWidget>

namespace gui::timer {
    class Gen4TimerPane : public QWidget {
        Q_OBJECT
    private:
        model::timer::Gen4TimerModel *model;
        const service::timer::DelayTimer *delayTimer;
        const service::CalibrationService *calibrationService;
        QSpinBox *delayHit;

    public:
        Gen4TimerPane(model::timer::Gen4TimerModel *model,
                      const service::timer::DelayTimer *delayTimer,
                      const service::CalibrationService *calibrationService,
                      QWidget *parent = nullptr);

        std::shared_ptr<std::vector<int>> createStages();

        void calibrate();

    private:
        void initComponents();

        int getCalibration() const;

    signals:
        void timerChanged(std::shared_ptr<std::vector<int>> stages);
    };
}  // namespace gui::timer

#endif  // EONTIMER_GEN4TIMERPANE_H
