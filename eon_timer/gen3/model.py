from enum import StrEnum
from typing import override

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


class Gen3Mode(EnhancedEnum, StrEnum):
    STANDARD = 'Standard'
    VARIABLE_TARGET = 'Variable Target'


@component()
class Gen3Model(Settings):
    mode = Property(Gen3Mode.STANDARD)
    pre_timer = Property(5000)
    target_frame = Property(1000)
    calibration = Property(0)
    frame_hit = Property(0, True)

    @property
    @override
    def group(self) -> str:
        return 'gen3'
