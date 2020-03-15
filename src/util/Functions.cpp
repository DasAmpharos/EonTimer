//
// Created by Dylan Meadows on 2020-03-12.
//

#include "Functions.h"

namespace util::functions {
    const int MINIMUM_LENGTH = 14000;

    int toMinimumLength(const int value) {
        int normalized = value;
        while (normalized < MINIMUM_LENGTH) {
            normalized += 60000;
        }
        return normalized;
    }
}
