#!/usr/bin/env python3

from setuptools import find_packages, setup

setup(
    name='EonTimer',
    version='3.0.0',
    packages=list(map(lambda it: f'eon_timer.{it}', find_packages('eon_timer'))),
    include_package_data=True,
    package_data={
        'eon_timer/resources': ['*.png', '*.zip'],
        'eon_timer/resources/fonts': ['*.ttf'],
        'eon_timer/resources/sounds': ['*.wav'],
    }
)
