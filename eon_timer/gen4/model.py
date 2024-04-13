from typing import override

from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


@component()
class Gen4Model(Settings):
    target_delay = Property(600)
    target_second = Property(50)
    calibrated_delay = Property(500)
    calibrated_second = Property(14)
    delay_hit = Property(0, True)

    @property
    @override
    def group(self) -> str:
        return 'gen4'
