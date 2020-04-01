#!/bin/bash

qmake EonTimer.pro
make -j $(sysctl -n hw.physicalcpu)