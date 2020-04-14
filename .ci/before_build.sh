#!/bin/bash

case $OS in
  linux)
    sh .ci/linux/before_build.sh
    ;;
  macOS)
    sh .ci/macos/before_build.sh
    ;;
esac
