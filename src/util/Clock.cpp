//
// Created by dmeadows on 5/2/20.
//

#include "Clock.h"

#include <iostream>

namespace util {
    Clock::Clock() { lastTick = std::chrono::high_resolution_clock::now(); }

    std::chrono::microseconds Clock::tick() {
        const auto now = std::chrono::high_resolution_clock::now();
        const auto lastTick =
            std::chrono::time_point_cast<std::chrono::microseconds>(
                this->lastTick)
                .time_since_epoch();
        const auto currentTick =
            std::chrono::time_point_cast<std::chrono::microseconds>(now)
                .time_since_epoch();
        this->lastTick = now;
        return std::chrono::duration_cast<std::chrono::microseconds>(
            currentTick - lastTick);
    }
}  // namespace util
