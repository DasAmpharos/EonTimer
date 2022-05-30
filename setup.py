#!/usr/bin/env python3

import os

from setuptools import find_packages, setup

src = os.path.abspath('src')
python = os.path.join(src, 'python')
resources = os.path.join(src, 'resources')


def get_resources():
    """ get list of resources to package together with the egg """
    cwd = os.getcwd()
    os.chdir(src)

    resources = []
    for dirname, dirs, files in os.walk('resources'):
        relname = os.path.sep.join(dirname.split(os.path.sep)[1:])
        files = filter(lambda it: not it.endswith('.py'), files)
        files = map(lambda it: os.path.join(relname, it), files)
        resources.extend(list(files))

    os.chdir(cwd)
    return resources


setup(
    name='eon-timer',
    version='0.0.1',
    package_dir={"": python},
    packages=find_packages(where=python),
    package_data={"resources": get_resources()},
    include_package_data=True
)
