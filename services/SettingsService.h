//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_SETTINGSSERVICE_H
#define EONTIMER_SETTINGSSERVICE_H

#include <QObject>
#include <models/Console.h>

namespace service::Settings {
    model::Console getConsole();

    void setConsole(model::Console console);
}

#endif //EONTIMER_SETTINGSSERVICE_H
