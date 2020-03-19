//
// Created by Dylan Meadows on 2020-03-19.
//

#ifndef EONTIMER_GEN5TIMERMODE_H
#define EONTIMER_GEN5TIMERMODE_H

#include <vector>

namespace model {
    enum Gen5TimerMode {
        STANDARD, C_GEAR, ENTRALINK, ENTRALINK_PLUS
    };

    const char *getName(Gen5TimerMode mode);

    const std::vector<Gen5TimerMode> &gen5TimerModes();
}

#endif //EONTIMER_GEN5TIMERMODE_H
