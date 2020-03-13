//
// Created by Dylan Meadows on 2020-03-10.
//

#ifndef EONTIMER_TIMERSTATE_H
#define EONTIMER_TIMERSTATE_H

namespace model {
    struct TimerState {
        TimerState(long duration, long remaining, bool isRunning);

        const long duration;
        const long remaining;
        const bool isRunning;
    };
}

#endif //EONTIMER_TIMERSTATE_H
