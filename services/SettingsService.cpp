//
// Created by Dylan Meadows on 2020-03-10.
//

#include "SettingsService.h"

namespace service {
    SettingsService::SettingsService() {
        console = model::Console::NDS;
    }

    model::Console SettingsService::getConsole() const {
        return console;
    }

    void SettingsService::setConsole(const model::Console console) {
        this->console = console;
    }
}
