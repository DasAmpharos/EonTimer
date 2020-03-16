//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUND_H
#define EONTIMER_SOUND_H

#include <vector>

namespace model {
    enum Sound {
        BEEP, DING, POP, TICK
    };

    const char *getName(Sound sound);

    const std::vector<Sound> &sounds();
}

#endif //EONTIMER_SOUND_H
