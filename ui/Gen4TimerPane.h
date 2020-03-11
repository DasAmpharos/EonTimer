//
// Created by Dylan Meadows on 2020-03-09.
//

#ifndef EONTIMER_GEN4TIMERPANE_H
#define EONTIMER_GEN4TIMERPANE_H

#include <QWidget>
#include "../services/TimerService.h"
#include "../services/CalibrationService.h"

class QSpinBox;

namespace ui {
    class Gen4TimerPane : public QWidget {
    Q_OBJECT
    private:
        int calibratedDelay;
        int calibratedSecond;
        int targetDelay;
        int targetSecond;

        service::TimerService *timerService;
        service::SettingsService *settingsService;
        service::CalibrationService *calibrationService;
    public:
        Gen4TimerPane(service::TimerService *timerService,
                      service::SettingsService *settingsService,
                      service::CalibrationService *calibrationService);

    private:
        void initComponents();

        void updateTimer();
    };
}


#endif //EONTIMER_GEN4TIMERPANE_H
