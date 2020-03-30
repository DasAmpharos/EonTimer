//
// Created by Dylan Meadows on 2020-03-15.
//

#include "ActionSettingsModel.h"

namespace model::settings {
    namespace ActionSettingsFields {
        const char *GROUP = "action";
        const char *SOUND = "sound";
        const char *INTERVAL = "interval";
        const char *COUNT = "count";

        namespace Defaults {
            const int SOUND = 0;
            const uint INTERVAL = 500;
            const uint COUNT = 6;
        }
    }

    ActionSettingsModel::ActionSettingsModel(QSettings *settings) {
        settings->beginGroup(ActionSettingsFields::GROUP);
        sound = model::sound(settings->value(ActionSettingsFields::SOUND,
                                             ActionSettingsFields::Defaults::SOUND).toUInt());
        interval = std::chrono::milliseconds(settings->value(ActionSettingsFields::INTERVAL,
                                                             ActionSettingsFields::Defaults::INTERVAL).toULongLong());
        count = settings->value(ActionSettingsFields::COUNT,
                                ActionSettingsFields::Defaults::COUNT).toUInt();
        settings->endGroup();
    }

    void ActionSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(ActionSettingsFields::GROUP);
        settings->setValue(ActionSettingsFields::SOUND, model::indexOf(sound));
        settings->setValue(ActionSettingsFields::INTERVAL, interval.count());
        settings->setValue(ActionSettingsFields::COUNT, count);
        settings->endGroup();
    }

    model::Sound ActionSettingsModel::getSound() const {
        return sound;
    }

    void ActionSettingsModel::setSound(const model::Sound sound) {
        this->sound = sound;
    }

    std::chrono::milliseconds ActionSettingsModel::getInterval() const {
        return interval;
    }

    void ActionSettingsModel::setInterval(const std::chrono::milliseconds &interval) {
        this->interval = interval;
    }

    uint ActionSettingsModel::getCount() const {
        return count;
    }

    void ActionSettingsModel::setCount(const uint count) {
        this->count = count;
    }
}
