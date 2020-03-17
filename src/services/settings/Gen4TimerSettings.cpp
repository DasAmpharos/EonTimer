//
// Created by Dylan Meadows on 2020-03-17.
//

#include <QtCore>
#include "Gen4TimerSettings.h"
#include <iostream>

namespace service::settings {
    const char *CALIBRATED_DELAY = "gen4/calibratedDelay";
    const char *CALIBRATED_SECOND = "gen4/calibratedSecond";
    const char *TARGET_DELAY = "gen4/targetDelay";
    const char *TARGET_SECOND = "gen4/targetSecond";

    const int DEFAULT_CALIBRATED_DELAY = 500;
    const int DEFAULT_CALIBRATED_SECOND = 14;
    const int DEFAULT_TARGET_DELAY = 600;
    const int DEFAULT_TARGET_SECOND = 50;

    Gen4TimerSettings::Gen4TimerSettings(QSettings *settings)
        : settings(settings) {
    }

    int Gen4TimerSettings::getCalibratedDelay() const {
        return settings->value(CALIBRATED_DELAY, DEFAULT_CALIBRATED_DELAY).toInt();
    }

    void Gen4TimerSettings::setCalibratedDelay(int calibratedDelay) {
        settings->setValue(CALIBRATED_DELAY, calibratedDelay);
    }

    int Gen4TimerSettings::getCalibratedSecond() const {
        return settings->value(CALIBRATED_SECOND, DEFAULT_CALIBRATED_SECOND).toInt();
    }

    void Gen4TimerSettings::setCalibratedSecond(int calibratedSecond) {
        settings->setValue(CALIBRATED_SECOND, calibratedSecond);
    }

    int Gen4TimerSettings::getTargetDelay() const {
        return settings->value(TARGET_DELAY, DEFAULT_TARGET_DELAY).toInt();
    }

    void Gen4TimerSettings::setTargetDelay(int targetDelay) {
        settings->setValue(TARGET_DELAY, targetDelay);
    }

    int Gen4TimerSettings::getTargetSecond() const {
        return settings->value(TARGET_SECOND, DEFAULT_TARGET_SECOND).toInt();
    }

    void Gen4TimerSettings::setTargetSecond(int targetSecond) {
        settings->setValue(TARGET_SECOND, targetSecond);
    }
}
