from enum import StrEnum
from typing import override

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property, IntProperty, FloatProperty
from eon_timer.util.properties.settings import Settings


class Gen5Mode(EnhancedEnum, StrEnum):
    STANDARD = 'Standard'
    C_GEAR = 'C-Gear'
    ENTRALINK = 'Entralink'
    ENTRALINK_PLUS = 'Entralink+'


@component()
class Gen5Model(Settings):
    mode = Property(Gen5Mode.STANDARD, value_type=str)
    calibration = IntProperty(-95)
    frame_calibration = IntProperty(0)
    entralink_calibration = IntProperty(256)
    target_delay = IntProperty(1200)
    target_second = IntProperty(50)
    target_advances = IntProperty(100)
    delay_hit = IntProperty(0, transient=True)
    second_hit = IntProperty(0, transient=True)
    advances_hit = IntProperty(0, transient=True)

    @property
    @override
    def group(self) -> str:
        return 'gen5'
