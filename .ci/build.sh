#!/bin/bash

cmake -DCMAKE_BUILD_TYPE=RELEASE .
case $OS in
  linux)
    sh .ci/linux/build.sh
    ;;  
  macOS)
    sh .ci/macos/build.sh
    ;;
esac
