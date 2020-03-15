//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CONSOLE_H
#define EONTIMER_CONSOLE_H

namespace model {
    enum Console {
        GBA, NDS
    };

    double getFps(Console console);

    double getFramerate(Console console);
}

#endif //EONTIMER_CONSOLE_H
