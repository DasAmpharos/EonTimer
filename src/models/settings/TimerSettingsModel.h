//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_TIMERSETTINGSMODEL_H
#define EONTIMER_TIMERSETTINGSMODEL_H

#include <QSettings>
#include <models/Console.h>
#include <chrono>

namespace model::settings {
    class TimerSettingsModel {
    private:
        model::Console console;
        std::chrono::milliseconds refreshInterval;
        bool precisionCalibrationEnabled;
    public:
        explicit TimerSettingsModel(QSettings *settings);

        void sync(QSettings *settings) const;

        model::Console getConsole() const;

        void setConsole(model::Console console);

        std::chrono::milliseconds getRefreshInterval() const;

        void setRefreshInterval(const std::chrono::milliseconds &refreshInterval);

        bool isPrecisionCalibrationEnabled() const;

        void setPrecisionCalibrationEnabled(bool precisionCalibrationEnabled);
    };
}


#endif //EONTIMER_TIMERSETTINGSMODEL_H
