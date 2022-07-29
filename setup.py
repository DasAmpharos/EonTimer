#!/usr/bin/env python3

import os
from setuptools import find_packages, setup

name = 'eon-timer'
package_name = name.replace('-', '_')
packages = find_packages(package_name)
packages = list(map(lambda it: f'{package_name}.{it}', packages))


def get_package_data() -> dict[str, list[str]]:
    pkg_path = os.path.join(package_name, 'res')
    pkg_name = pkg_path.replace(os.path.pathsep, '.')

    files = os.listdir(pkg_path)
    resources = list(filter(lambda it: not it.endswith('.py'), files))
    return {pkg_name: resources}


setup(
    name=name,
    version='0.0.1',
    packages=packages,
    include_package_data=True,
    package_data=get_package_data()
)
