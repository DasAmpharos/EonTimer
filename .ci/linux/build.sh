#!/bin/bash

. /opt/qt5*/bin/qt5*-env.sh
qmake EonTimer.pro
make -j $(nproc)