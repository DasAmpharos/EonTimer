from enum import StrEnum
from typing import Final, override

from PySide6.QtGui import QColor

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property, EnumProperty, IntProperty
from eon_timer.util.properties.settings import Settings


class ActionMode(EnhancedEnum, StrEnum):
    AV = 'A/V'
    AUDIO = 'Audio'
    VISUAL = 'Visual'


class ActionSound(EnhancedEnum, StrEnum):
    BEEP = 'Beep'
    DING = 'Ding'
    POP = 'Pop'
    TICK = 'Tick'
    CUSTOM = 'Custom'


@component()
class ActionSettingsModel(Settings):
    mode: Final = EnumProperty(ActionMode.AV)
    sound: Final = EnumProperty(ActionSound.BEEP)
    color: Final = Property(QColor(0, 0, 255))
    custom_sound: Final = Property(None, value_type=str)
    interval: Final = IntProperty(500)
    count: Final = IntProperty(6)

    @property
    @override
    def group(self) -> str:
        return 'action'
