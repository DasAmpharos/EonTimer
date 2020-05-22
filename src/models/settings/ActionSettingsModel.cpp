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
            const unsigned int INTERVAL = 500;
            const unsigned int COUNT = 6;
        }  // namespace Defaults
    }      // namespace ActionSettingsFields

    ActionSettingsModel::ActionSettingsModel(QSettings *settings) : QObject(nullptr) {
        settings->beginGroup(ActionSettingsFields::GROUP);
        mode = model::actionMode(
            settings->value(ActionSettingsFields::MODE, ActionSettingsFields::Defaults::MODE).toUInt());
        sound =
            model::sound(settings->value(ActionSettingsFields::SOUND, ActionSettingsFields::Defaults::SOUND).toUInt());
        color = settings->value(ActionSettingsFields::COLOR, ActionSettingsFields::Defaults::COLOR).value<QColor>();
        interval = settings->value(ActionSettingsFields::INTERVAL, ActionSettingsFields::Defaults::INTERVAL).toUInt();
        count = settings->value(ActionSettingsFields::COUNT, ActionSettingsFields::Defaults::COUNT).toUInt();
        settings->endGroup();
    }

    void ActionSettingsModel::sync(QSettings *settings) const {
        settings->beginGroup(ActionSettingsFields::GROUP);
        settings->setValue(ActionSettingsFields::MODE, model::indexOf(mode));
        settings->setValue(ActionSettingsFields::SOUND, model::indexOf(sound));
        settings->setValue(ActionSettingsFields::INTERVAL, interval);
        settings->setValue(ActionSettingsFields::COUNT, count);
        settings->setValue(ActionSettingsFields::COLOR, color);
        settings->endGroup();
    }

    model::ActionMode ActionSettingsModel::getMode() const { return mode; }

    void ActionSettingsModel::setMode(model::ActionMode mode) { this->mode = mode; }

    model::Sound ActionSettingsModel::getSound() const { return sound; }

    void ActionSettingsModel::setSound(const model::Sound sound) { this->sound = sound; }

    const QColor &ActionSettingsModel::getColor() const { return color; }

    void ActionSettingsModel::setColor(const QColor &color) {
        if (this->color != color) {
            this->color = color;
            emit colorChanged(color);
        }
    }

    unsigned int ActionSettingsModel::getInterval() const { return interval; }

    void ActionSettingsModel::setInterval(const unsigned int interval) { this->interval = interval; }

    unsigned int ActionSettingsModel::getCount() const { return count; }

    void ActionSettingsModel::setCount(const unsigned int count) { this->count = count; }
}  // namespace model::settings
