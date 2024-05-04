from enum import StrEnum
from typing import override

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import IntProperty, FloatProperty, EnumProperty
from eon_timer.util.properties.settings import Settings


class Gen3Mode(EnhancedEnum, StrEnum):
    STANDARD = 'Standard'
    VARIABLE_TARGET = 'Variable Target'


@component()
class Gen3Model(Settings):
    mode = EnumProperty(Gen3Mode.STANDARD)
    pre_timer = IntProperty(5000)
    target_frame = IntProperty(1000)
    calibration = FloatProperty(0.0)
    frame_hit = IntProperty(0, transient=True)

    @property
    @override
    def group(self) -> str:
        return 'gen3'
