from typing import override

from eon_timer.util.injector import component
from eon_timer.util.properties.property import IntProperty
from eon_timer.util.properties.settings import Settings


@component()
class Gen4Model(Settings):
    target_delay = IntProperty(600)
    target_second = IntProperty(50)
    calibrated_delay = IntProperty(500)
    calibrated_second = IntProperty(14)
    delay_hit = IntProperty(None, transient=True)

    @property
    @override
    def group(self) -> str:
        return 'gen4'
