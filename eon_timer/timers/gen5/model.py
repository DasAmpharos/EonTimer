from enum import StrEnum
from typing import override

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


class Gen5Mode(EnhancedEnum, StrEnum):
    STANDARD = 'Standard'
    C_GEAR = 'C-Gear'
    ENTRALINK = 'Entralink'
    ENTRALINK_PLUS = 'Entralink+'


@component()
class Gen5Model(Settings):
    mode = Property(Gen5Mode.STANDARD, value_type=str)
    calibration = Property(-95)
    frame_calibration = Property(0)
    entralink_calibration = Property(256)
    target_delay = Property(1200)
    target_second = Property(50)
    target_advances = Property(100)
    delay_hit = Property(0, transient=True)
    second_hit = Property(0, transient=True)
    advances_hit = Property(0, transient=True)

    @property
    @override
    def group(self) -> str:
        return 'gen5'
