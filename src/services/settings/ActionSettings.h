//
// Created by Dylan Meadows on 2020-03-15.
//

#ifndef EONTIMER_ACTIONSETTINGS_H
#define EONTIMER_ACTIONSETTINGS_H

#include <QSettings>
#include <models/Sound.h>
#include <chrono>

namespace service::settings {
    class ActionSettings {
    private:
        QSettings *settings;
    public:
        explicit ActionSettings(QSettings *settings);

        model::Sound getSound() const;

        void setSound(model::Sound sound);

        std::chrono::milliseconds getInterval() const;

        void setInterval(const std::chrono::milliseconds &interval);

        uint getCount() const;

        void setCount(uint count);
    };
}


#endif //EONTIMER_ACTIONSETTINGS_H
