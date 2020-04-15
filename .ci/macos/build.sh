#!/bin/bash

make -j $(sysctl -n hw.physicalcpu)
macdeployqt EonTimer.app -dmg -verbose=2
