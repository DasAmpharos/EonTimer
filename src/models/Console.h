//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_CONSOLE_H
#define EONTIMER_CONSOLE_H

#include <vector>

namespace model {
    enum Console {
        NDS, GBA
    };

    double getFps(Console console);

    double getFramerate(Console console);

    const std::vector<Console> &consoles();
}

#endif //EONTIMER_CONSOLE_H
