//
// Created by Dylan Meadows on 2020-03-10.
//

#include "SettingsService.h"

namespace service::Settings {
    model::Console mConsole = model::Console::NDS;

    model::Console getConsole() {
        return mConsole;
    }

    void setConsole(const model::Console console) {
        mConsole = console;
    }
}
