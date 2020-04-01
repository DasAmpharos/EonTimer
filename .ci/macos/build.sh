#!/bin/bash

qmake EonTimer.pro
make -j $(sysctl -n hw.physicalcpu)
macdeployqt EonTimer.app -dmg -verbose=2