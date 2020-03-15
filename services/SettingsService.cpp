//
// Created by Dylan Meadows on 2020-03-10.
//

#include "SettingsService.h"

namespace service {
    SettingsService::SettingsService()
        : actionCount(6),
          actionInterval(std::chrono::milliseconds(500)),
          refreshInterval(std::chrono::milliseconds(8)),
          console(model::Console::NDS),
          sound(model::Sound::BEEP) {
    }

    SettingsService &SettingsService::get() {
        static SettingsService instance;
        return instance;
    }

    model::Console SettingsService::getConsole() const {
        return console;
    }

    void SettingsService::setConsole(model::Console console) {
        SettingsService::console = console;
    }

    const std::chrono::milliseconds &SettingsService::getActionInterval() const {
        return actionInterval;
    }

    void SettingsService::setActionInterval(const std::chrono::milliseconds &actionInterval) {
        SettingsService::actionInterval = actionInterval;
    }

    const std::chrono::milliseconds &SettingsService::getRefreshInterval() const {
        return refreshInterval;
    }

    void SettingsService::setRefreshInterval(const std::chrono::milliseconds &refreshInterval) {
        SettingsService::refreshInterval = refreshInterval;
    }

    uint8_t SettingsService::getActionCount() const {
        return actionCount;
    }

    void SettingsService::setActionCount(uint8_t actionCount) {
        SettingsService::actionCount = actionCount;
    }

    model::Sound SettingsService::getSound() const {
        return sound;
    }

    void SettingsService::setSound(model::Sound sound) {
        SettingsService::sound = sound;
    }
}
