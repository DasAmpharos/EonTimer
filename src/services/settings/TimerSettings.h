//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_TIMERSETTINGS_H
#define EONTIMER_TIMERSETTINGS_H

#include <QSettings>
#include <models/Console.h>
#include <chrono>

namespace service::settings {
    class TimerSettings {
    private:
        QSettings *settings;
    public:
        explicit TimerSettings(QSettings *settings);

        model::Console getConsole() const;

        void setConsole(model::Console console);

        std::chrono::milliseconds getRefreshInterval() const;

        void setRefreshInterval(const std::chrono::milliseconds &refreshInterval);

        bool isPrecisionCalibrationEnabled() const;

        void setPrecisionCalibrationEnabled(bool precisionCalibrationEnabled);
    };
}


#endif //EONTIMER_TIMERSETTINGS_H
