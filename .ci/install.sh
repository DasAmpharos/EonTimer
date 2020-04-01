#!/bin/bash

echo $OS
case $OS in
  linux)
    sh linux/install.sh
    ;;  
  macOS)
    sh macos/install.sh
    ;;
esac