//
// Created by Dylan Meadows on 2020-03-16.
//

#include <string>
#include "Sound.h"

namespace model {
    const std::vector<Sound> VALUES{Sound::BEEP, Sound::DING, Sound::TICK, Sound::POP};
    namespace names {
        const char *BEEP = "Beep";
        const char *DING = "Ding";
        const char *TICK = "Tick";
        const char *POP = "Pop";
    }

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

    const std::vector<Sound> &sounds() {
        return VALUES;
    }
}
