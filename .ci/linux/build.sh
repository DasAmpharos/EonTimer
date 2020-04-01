#!/bin/bash

qmake EonTimer.pro
make -j $(nproc)