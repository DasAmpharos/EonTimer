#!/bin/bash

sudo add-apt-repository ppa:beineri/opt-qt-5.14.1-bionic -y && sudo apt-get update
sudo apt-get install qt514base qt514tools libgl1-mesa-dev libsfml-dev -y
