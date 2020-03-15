//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_SETTINGSSERVICE_H
#define EONTIMER_SETTINGSSERVICE_H

#include <models/Console.h>
#include <chrono>
#include <models/Sound.h>

namespace service {
    class SettingsService {
    private:
        uint8_t actionCount;
        std::chrono::milliseconds actionInterval;
        std::chrono::milliseconds refreshInterval;
        model::Console console;
        model::Sound sound;
    private:
        SettingsService();

    public:
        static SettingsService &get();

        uint8_t getActionCount() const;

        void setActionCount(uint8_t actionCount);

        const std::chrono::milliseconds &getActionInterval() const;

        void setActionInterval(const std::chrono::milliseconds &actionInterval);

        const std::chrono::milliseconds &getRefreshInterval() const;

        void setRefreshInterval(const std::chrono::milliseconds &refreshInterval);

        model::Console getConsole() const;

        void setConsole(model::Console console);

        model::Sound getSound() const;

        void setSound(model::Sound sound);
    };
}

#endif //EONTIMER_SETTINGSSERVICE_H
