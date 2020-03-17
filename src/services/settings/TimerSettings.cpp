//
// Created by Dylan Meadows on 2020-03-15.
//

#include "TimerSettings.h"
#include <QtCore>

namespace service::settings {
    const char *CONSOLE = "timer/console";
    const char *PRECISION_CALIBRATION_ENABLED = "timer/precisionCalibrationEnabled";
    const char *REFRESH_INTERVAL = "timer/refreshInterval";

    const model::Console DEFAULT_CONSOLE = model::Console::NDS;
    const bool DEFAULT_PRECISION_CALIBRATION_ENABLED = false;
    const uint DEFAULT_REFRESH_INTERVAL = 8;

    TimerSettings::TimerSettings(QSettings *settings)
        : settings(settings) {
        qApp->connect(qApp, &QCoreApplication::aboutToQuit, [settings] {
            settings->sync();
        });
    }

    model::Console TimerSettings::getConsole() const {
        return static_cast<model::Console>(settings->value(CONSOLE, DEFAULT_CONSOLE).toUInt());
    }

    void TimerSettings::setConsole(const model::Console console) {
        settings->setValue(CONSOLE, console);
    }

    std::chrono::milliseconds TimerSettings::getRefreshInterval() const {
        return std::chrono::milliseconds(settings->value(REFRESH_INTERVAL, DEFAULT_REFRESH_INTERVAL).toUInt());
    }

    void TimerSettings::setRefreshInterval(const std::chrono::milliseconds &refreshInterval) {
        settings->setValue(REFRESH_INTERVAL, static_cast<qint64>(refreshInterval.count()));
    }

    bool TimerSettings::isPrecisionCalibrationEnabled() const {
        return settings->value(PRECISION_CALIBRATION_ENABLED, DEFAULT_PRECISION_CALIBRATION_ENABLED).toBool();
    }

    void TimerSettings::setPrecisionCalibrationEnabled(const bool precisionCalibrationEnabled) {
        settings->setValue(PRECISION_CALIBRATION_ENABLED, precisionCalibrationEnabled);
    }
}
