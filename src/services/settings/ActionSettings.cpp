//
// Created by Dylan Meadows on 2020-03-15.
//

#include "ActionSettings.h"

namespace service::settings {
    const char *SOUND = "action/sound";
    const char *INTERVAL = "action/interval";
    const char *COUNT = "action/count";

    const model::Sound DEFAULT_SOUND = model::Sound::BEEP;
    const uint DEFAULT_INTERVAL = 500;
    const uint DEFAULT_COUNT = 6;


    ActionSettings::ActionSettings(QSettings *settings)
        : settings(settings) {
    }

    model::Sound ActionSettings::getSound() const {
        return static_cast<model::Sound>(settings->value(SOUND, DEFAULT_SOUND).toUInt());
    }

    void ActionSettings::setSound(const model::Sound sound) {
        settings->setValue(SOUND, sound);
        settings->sync();
    }

    std::chrono::milliseconds ActionSettings::getInterval() const {
        return std::chrono::milliseconds(settings->value(INTERVAL, DEFAULT_INTERVAL).toUInt());
    }

    void ActionSettings::setInterval(const std::chrono::milliseconds &interval) {
        settings->setValue(INTERVAL, static_cast<qint64>(interval.count()));
        settings->sync();
    }

    uint ActionSettings::getCount() const {
        return settings->value(COUNT, DEFAULT_COUNT).toUInt();
    }

    void ActionSettings::setCount(const uint count) {
        settings->setValue(COUNT, count);
        settings->sync();
    }
}
