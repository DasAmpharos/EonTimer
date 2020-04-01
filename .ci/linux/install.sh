#!/bin/bash

add-apt-repository ppa:beineri/opt-qt-5.14.1-bionic -y && apt-get update
apt-get install qt514base libsfml-dev -y
source /opt/qt5*/bin/qt5*-env.sh
