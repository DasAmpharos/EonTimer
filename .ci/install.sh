#!/bin/bash

echo $OS
case $OS in
  linux)
    sh .ci/linux/install.sh
    ;;  
  macOS)
    sh .ci/macos/install.sh
    ;;
esac