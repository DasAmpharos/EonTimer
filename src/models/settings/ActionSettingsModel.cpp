//
// Created by Dylan Meadows on 2020-03-15.
//

#include "ActionSettingsModel.h"

namespace model::settings {
    namespace ActionSettingsFields {
        const char *GROUP = "action";

        const char *MODE = "mode";
        const char *SOUND = "sound";
        const char *COLOR = "color";
        const char *INTERVAL = "interval";
        const char *COUNT = "count";

        namespace Defaults {
            const int MODE = 0;
            const int SOUND = 0;
            const QColor COLOR = Qt::blue;
            const uint INTERVAL = 500;
            const uint COUNT = 6;
        }  // namespace Defaults
    }      // namespace ActionSettingsFields

    ActionSettingsModel::ActionSettingsModel(QSettings *settings) {
        settings->beginGroup(ActionSettingsFields::GROUP);
        mode =
            model::actionMode(settings
                                  ->value(ActionSettingsFields::MODE,
                                          ActionSettingsFields::Defaults::MODE)
                                  .toUInt());
        sound = model::sound(settings
                                 ->value(ActionSettingsFields::SOUND,
                                         ActionSettingsFields::Defaults::SOUND)
                                 .toUInt());
        color = settings
                    ->value(ActionSettingsFields::COLOR,
                            ActionSettingsFields::Defaults::COLOR)
                    .value<QColor>();
        interval = std::chrono::milliseconds(
            settings
                ->value(ActionSettingsFields::INTERVAL,
                        ActionSettingsFields::Defaults::INTERVAL)
                .toULongLong());
        count = settings
                    ->value(ActionSettingsFields::COUNT,
                            ActionSettingsFields::Defaults::COUNT)
                    .toUInt();
        settings->endGroup();
    }

    void ActionSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(ActionSettingsFields::GROUP);
        settings->setValue(ActionSettingsFields::MODE, model::indexOf(mode));
        settings->setValue(ActionSettingsFields::SOUND, model::indexOf(sound));
        settings->setValue(ActionSettingsFields::INTERVAL,
                           static_cast<int>(interval.count()));
        settings->setValue(ActionSettingsFields::COUNT, count);
        settings->setValue(ActionSettingsFields::COLOR, color);
        settings->endGroup();
    }

    model::ActionMode ActionSettingsModel::getMode() const { return mode; }

    void ActionSettingsModel::setMode(model::ActionMode mode) {
        this->mode = mode;
    }

    model::Sound ActionSettingsModel::getSound() const { return sound; }

    void ActionSettingsModel::setSound(const model::Sound sound) {
        this->sound = sound;
    }

    const QColor &ActionSettingsModel::getColor() const { return color; }

    void ActionSettingsModel::setColor(const QColor &color) {
        this->color = color;
    }

    std::chrono::milliseconds ActionSettingsModel::getInterval() const {
        return interval;
    }

    void ActionSettingsModel::setInterval(
        const std::chrono::milliseconds &interval) {
        this->interval = interval;
    }

    uint ActionSettingsModel::getCount() const { return count; }

    void ActionSettingsModel::setCount(const uint count) {
        this->count = count;
    }
}  // namespace model::settings
