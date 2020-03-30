//
// Created by Dylan Meadows on 2020-03-14.
//

#ifndef EONTIMER_SOUND_H
#define EONTIMER_SOUND_H

#include <vector>
#include <QtGlobal>

namespace model {
    enum Sound {
        BEEP, DING, POP, TICK
    };

    const char *getName(Sound sound);

    Sound sound(uint index);

    const std::vector<Sound> &sounds();

    int indexOf(Sound sound);
}

#endif //EONTIMER_SOUND_H
