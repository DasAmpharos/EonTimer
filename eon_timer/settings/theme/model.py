from enum import StrEnum

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


class Theme(EnhancedEnum, StrEnum):
    DEFAULT = 'Default'
    SYSTEM = 'System'


@component()
class ThemeSettingsModel(Settings):
    theme = Property(Theme.DEFAULT, value_type=str)

    @property
    def group(self) -> str:
        return 'theme'
