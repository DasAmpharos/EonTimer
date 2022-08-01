#!/usr/bin/env python3

import os
from setuptools import find_packages, setup

name = 'eon-timer'
package_name = name.replace('-', '_')
packages = find_packages(package_name)
packages = list(map(lambda it: f'{package_name}.{it}', packages))


def get_package_data() -> dict[str, list[str]]:
    resources = []
    pkg_path = os.path.join(package_name, 'resources')
    for dirname, _, files in os.walk(pkg_path):
        if '__pycache__' not in dirname:
            files = filter(lambda it: not it.endswith('.py'), files)
            file_paths = list(map(lambda it: os.path.join(dirname, it), files))
            resources.extend(file_paths)
    pkg_name = pkg_path.replace(os.path.pathsep, '.')
    return {pkg_name: resources}


setup(
    name=name,
    version='3.0.0',
    packages=packages,
    include_package_data=True,
    package_data=get_package_data()
)
