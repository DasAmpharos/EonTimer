//
// Created by Dylan Meadows on 2020-03-12.
//

#include "TimerState.h"

namespace model {
    TimerState::TimerState(const long duration, const long remaining, const bool isRunning)
        : duration(duration),
          remaining(remaining),
          isRunning(isRunning) {
    }
}
