from enum import StrEnum
from typing import Final, override

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings

GBA_FPS: Final[float] = 59.7275
NDS_SLOT1_FPS: Final[float] = 59.8261
NDS_SLOT2_FPS: Final[float] = 59.6555

GBA_FRAMERATE: Final[float] = 1000 / GBA_FPS
NDS_SLOT1_FRAMERATE: Final[float] = 1000 / NDS_SLOT1_FPS
NDS_SLOT2_FRAMERATE: Final[float] = 1000 / NDS_SLOT2_FPS


class Console(EnhancedEnum, StrEnum):
    GBA = 'GBA'
    NDS_SLOT1 = 'NDS - Slot 1'
    NDS_SLOT2 = 'NDS - Slot 2'
    DSI = 'DSI'
    THREE_DS = '3DS'

    @property
    def fps(self) -> float:
        match self:
            case self.GBA:
                return GBA_FPS
            case self.NDS_SLOT2:
                return NDS_SLOT2_FPS
            case self.NDS_SLOT1 | self.DSI | self.THREE_DS:
                return NDS_SLOT1_FPS

    @property
    def framerate(self) -> float:
        match self:
            case self.GBA:
                return GBA_FRAMERATE
            case self.NDS_SLOT2:
                return NDS_SLOT2_FRAMERATE
            case self.NDS_SLOT1 | self.DSI | self.THREE_DS:
                return NDS_SLOT1_FRAMERATE


@component()
class TimerSettingsModel(Settings):
    console = Property(Console.NDS_SLOT1)
    precision_calibration = Property(False)
    refresh_interval = Property(8)

    @property
    @override
    def group(self) -> str:
        return 'timer'
