#!/bin/bash

zip -r EonTimer-$OS.zip EonTimer.dmg
shasum -a 256 EonTimer-$OS.zip > EonTimer-$OS.zip.sha256