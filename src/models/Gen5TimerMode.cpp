//
// Created by Dylan Meadows on 2020-03-19.
//

#include <ntsid.h>
#include "Gen5TimerMode.h"

namespace model {
    const std::vector<Gen5TimerMode> VALUES{Gen5TimerMode::STANDARD,
                                            Gen5TimerMode::C_GEAR,
                                            Gen5TimerMode::ENTRALINK,
                                            Gen5TimerMode::ENTRALINK_PLUS};
    namespace names {
        const char *STANDARD = "Standard";
        const char *C_GEAR = "C-Gear";
        const char *ENTRALINK = "Entralink";
        const char *ENTRALINK_PLUS = "Entralink+";
    }

    const char *getName(const Gen5TimerMode mode) {
        switch (mode) {
            case STANDARD:
                return names::STANDARD;
            case C_GEAR:
                return names::C_GEAR;
            case ENTRALINK:
                return names::ENTRALINK;
            case ENTRALINK_PLUS:
                return names::ENTRALINK_PLUS;
            default:
                return nullptr;
        }
    }

    Gen5TimerMode gen5TimerMode(int index) {
        return VALUES[index];
    }

    const std::vector<Gen5TimerMode> &gen5TimerModes() {
        return VALUES;
    }

    int indexOf(const Gen5TimerMode mode) {
        for (int i = 0; i < VALUES.size(); i++) {
            if (mode == VALUES[i]) return i;
        }
        return -1;
    }
}
