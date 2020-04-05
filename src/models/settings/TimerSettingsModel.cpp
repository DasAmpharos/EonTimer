//
// Created by Dylan Meadows on 2020-03-15.
//

#include "TimerSettingsModel.h"

namespace model::settings {
    namespace TimerSettingsFields {
        const char *GROUP = "timer";
        const char *CONSOLE = "console";
        const char *REFRESH_INTERVAL = "refreshInterval";
        const char *PRECISION_CALIBRATION_ENABLED =
            "precisionCalibrationEnabled";

        namespace Defaults {
            const int CONSOLE = 1;
            const int REFRESH_INTERVAL = 8;
            const bool PRECISION_CALIBRATION_ENABLED = false;
        }  // namespace Defaults
    }      // namespace TimerSettingsFields

    TimerSettingsModel::TimerSettingsModel(QSettings *settings) {
        settings->beginGroup(TimerSettingsFields::GROUP);
        console =
            model::console(settings
                               ->value(TimerSettingsFields::CONSOLE,
                                       TimerSettingsFields::Defaults::CONSOLE)
                               .toInt());
        refreshInterval = std::chrono::milliseconds(
            settings
                ->value(TimerSettingsFields::REFRESH_INTERVAL,
                        TimerSettingsFields::Defaults::REFRESH_INTERVAL)
                .toULongLong());
        precisionCalibrationEnabled =
            settings
                ->value(TimerSettingsFields::PRECISION_CALIBRATION_ENABLED,
                        TimerSettingsFields::Defaults::
                            PRECISION_CALIBRATION_ENABLED)
                .toBool();
        settings->endGroup();
    }

    void TimerSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(TimerSettingsFields::GROUP);
        settings->setValue(TimerSettingsFields::CONSOLE,
                           model::indexOf(console));
        settings->setValue(TimerSettingsFields::REFRESH_INTERVAL,
                           static_cast<int>(refreshInterval.count()));
        settings->setValue(TimerSettingsFields::PRECISION_CALIBRATION_ENABLED,
                           precisionCalibrationEnabled);
        settings->endGroup();
    }

    model::Console TimerSettingsModel::getConsole() const { return console; }

    void TimerSettingsModel::setConsole(const model::Console console) {
        this->console = console;
    }

    std::chrono::milliseconds TimerSettingsModel::getRefreshInterval() const {
        return refreshInterval;
    }

    void TimerSettingsModel::setRefreshInterval(
        const std::chrono::milliseconds &refreshInterval) {
        this->refreshInterval = refreshInterval;
    }

    bool TimerSettingsModel::isPrecisionCalibrationEnabled() const {
        return precisionCalibrationEnabled;
    }

    void TimerSettingsModel::setPrecisionCalibrationEnabled(
        const bool precisionCalibrationEnabled) {
        this->precisionCalibrationEnabled = precisionCalibrationEnabled;
    }
}  // namespace model::settings
