//
// Created by Dylan Meadows on 2020-03-12.
//

#ifndef EONTIMER_FUNCTIONS_H
#define EONTIMER_FUNCTIONS_H

#include <string>

namespace util::functions {
    int toMinimumLength(int value);

    bool equalsIgnoreCase(const char *s1, const char *s2);

    bool equalsIgnoreCase(const std::string &s1, const std::string &s2);
}  // namespace util::functions

#endif  // EONTIMER_FUNCTIONS_H
