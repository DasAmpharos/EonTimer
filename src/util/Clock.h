//
// Created by dmeadows on 5/2/20.
//

#ifndef EONTIMER_CLOCK_H
#define EONTIMER_CLOCK_H

#include <chrono>

namespace util {
    class Clock {
    public:
        Clock();

        std::chrono::microseconds tick();

    private:
        std::chrono::time_point<std::chrono::high_resolution_clock> lastTick;
    };
}  // namespace util

#endif  // EONTIMER_CLOCK_H
