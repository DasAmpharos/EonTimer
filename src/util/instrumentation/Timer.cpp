//
// Created by dmeadows on 5/2/20.
//

#include "Timer.h"

#include <iostream>

namespace util::instrumentation {
    Timer::Timer(const char *operation) : operation(operation) {
        startTimePoint = std::chrono::high_resolution_clock::now();
        running = true;
    }

    Timer::~Timer() { stop(); }

    void Timer::stop() {
        if (running) {
            auto endTimePoint = std::chrono::high_resolution_clock::now();
            auto start =
                std::chrono::time_point_cast<std::chrono::microseconds>(startTimePoint).time_since_epoch().count();
            auto end = std::chrono::time_point_cast<std::chrono::microseconds>(endTimePoint).time_since_epoch().count();

            auto duration = end - start;
            double ms = duration * 0.001;

            std::cout << operation << ": ";
            std::cout << duration << "us (" << ms << "ms)\n";
            running = false;
        }
    }
}  // namespace util::instrumentation
