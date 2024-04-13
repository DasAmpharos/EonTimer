from enum import StrEnum
from typing import Final, override

from PySide6.QtGui import QColor

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
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
    mode: Final = Property(ActionMode.AV)
    sound: Final = Property(ActionSound.BEEP)
    color: Final = Property(QColor(0, 0, 255))
    custom_sound: Final = Property(None)
    interval: Final = Property(500)
    count: Final = Property(6)

    @property
    @override
    def group(self) -> str:
        return 'action'
