import os
from typing import Final

from .. import PACKAGE as parent_package
from ... import util

PACKAGE: Final[str] = '.'.join([parent_package, util.get_module_name(__file__)])


def resource_filename(filename: str) -> str:
    directory = os.path.dirname(__file__)
    return os.path.join(directory, filename)
