//
// Created by Dylan Meadows on 2020-03-16.
//

#ifndef EONTIMER_TIMERSETTINGSPANE_H
#define EONTIMER_TIMERSETTINGSPANE_H

#include <QWidget>
#include <services/settings/TimerSettings.h>

namespace gui::settings {
    class TimerSettingsPane : public QWidget {
    private:
        service::settings::TimerSettings *settings;
    public:
        TimerSettingsPane(service::settings::TimerSettings *settings,
                          QWidget *parent = nullptr);

        void sync();
    private:
        void initComponents();
    };
}

#endif //EONTIMER_TIMERSETTINGSPANE_H
