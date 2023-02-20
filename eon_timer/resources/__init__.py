from typing import Final

from .. import PACKAGE as parent_package
from .. import util

PACKAGE: Final[str] = '.'.join([
    parent_package,
    util.get_module_name(__file__)
])
