//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_ACTIONSETTINGSMODEL_H
#define EONTIMER_ACTIONSETTINGSMODEL_H

#include <models/Sound.h>

#include <QSettings>
#include <chrono>

namespace model::settings {
    class ActionSettingsModel {
    private:
        model::Sound sound;
        std::chrono::milliseconds interval;
        uint count;

    public:
        explicit ActionSettingsModel(QSettings *settings);

        void sync(QSettings *settings) const;

        model::Sound getSound() const;

        void setSound(model::Sound sound);

        std::chrono::milliseconds getInterval() const;

        void setInterval(const std::chrono::milliseconds &interval);

        uint getCount() const;

        void setCount(uint count);
    };
}  // namespace model::settings

#endif  // EONTIMER_ACTIONSETTINGSMODEL_H
