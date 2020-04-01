#!/bin/bash

case $OS in
  linux)
    sh .ci/linux/after_build.sh
    ;;  
  macOS)
    sh .ci/macos/after_build.sh
    ;;
esac