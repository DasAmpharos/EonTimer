//
// Created by Dylan Meadows on 2020-03-16.
//

#include "Sound.h"

#include <string>

namespace model {
    const std::vector<Sound> VALUES{Sound::BEEP, Sound::DING, Sound::TICK,
                                    Sound::POP};
    namespace names {
        const char *BEEP = "Beep";
        const char *DING = "Ding";
        const char *TICK = "Tick";
        const char *POP = "Pop";
    }  // namespace names

    const char *getName(const Sound sound) {
        switch (sound) {
            case BEEP:
                return names::BEEP;
            case DING:
                return names::DING;
            case TICK:
                return names::TICK;
            case POP:
                return names::POP;
            default:
                return "";
        }
    }

    Sound sound(const uint index) { return VALUES[index]; }

    const std::vector<Sound> &sounds() { return VALUES; }

    int indexOf(const Sound sound) {
        for (uint i = 0; i < VALUES.size(); i++) {
            if (sound == VALUES[i]) return i;
        }
        return -1;
    }
}  // namespace model
