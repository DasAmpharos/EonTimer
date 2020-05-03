//
// Created by dmeadows on 5/2/20.
//

#ifndef EONTIMER_TIMER_H
#define EONTIMER_TIMER_H

#include <chrono>

namespace util::instrumentation {
    class Timer {
    public:
        explicit Timer(const char *operation);

        ~Timer();

    private:
        const char *operation;
        std::chrono::time_point<std::chrono::high_resolution_clock>
            startTimePoint;
        bool running;

        void stop();
    };
}  // namespace util::instrumentation

#endif  // EONTIMER_TIMER_H
