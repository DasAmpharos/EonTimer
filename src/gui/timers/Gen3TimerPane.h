//
// Created by Dylan Meadows on 2020-03-26.
//

#ifndef EONTIMER_GEN3TIMERPANE_H
#define EONTIMER_GEN3TIMERPANE_H

#include <models/timers/Gen3TimerModel.h>
#include <services/CalibrationService.h>
#include <services/TimerService.h>
#include <services/timers/FrameTimer.h>

#include <QSpinBox>
#include <QWidget>

namespace gui::timer {
    class Gen3TimerPane : public QWidget {
        Q_OBJECT
    private:
        model::timer::Gen3TimerModel *model;
        const service::timer::FrameTimer *frameTimer;
        const service::CalibrationService *calibrationService;

    public:
        Gen3TimerPane(model::timer::Gen3TimerModel *model,
                      const service::timer::FrameTimer *frameTimer,
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

#endif  // EONTIMER_GEN3TIMERPANE_H
