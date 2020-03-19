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

    bool equalsIgnoreCase(const char *s1, const char *s2) {
        return equalsIgnoreCase(std::string(s1), std::string(s2));
    }

    bool equalsIgnoreCase(const std::string &s1, const std::string &s2) {
        return std::equal(s1.begin(), s1.end(),
                          s2.begin(), s2.end(),
                          [](char c1, char c2) {
                              return tolower(c1) == tolower(c2);
                          });;
    }
}
