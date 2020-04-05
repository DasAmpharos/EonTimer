//
// Created by Dylan Meadows on 2020-03-12.
//

#include "TimerState.h"

namespace model {
    TimerState::TimerState(const std::chrono::milliseconds &duration,
                           const std::chrono::milliseconds &remaining)
        : duration(duration), remaining(remaining) {}
}  // namespace model
