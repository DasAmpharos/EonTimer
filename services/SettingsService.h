//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_SETTINGSSERVICE_H
#define EONTIMER_SETTINGSSERVICE_H

#include "../models/Console.h"

namespace service {
    class SettingsService {
    private:
        model::Console console;
    public:
        SettingsService();

        model::Console getConsole() const;

        void setConsole(model::Console console);
    };
}

#endif //EONTIMER_SETTINGSSERVICE_H
