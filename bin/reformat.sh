#!/bin/bash

find $1 -iname "*.h" -o -iname "*.cpp" | xargs clang-format -i
