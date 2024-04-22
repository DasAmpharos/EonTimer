from eon_timer.util.injector import component
from eon_timer.util.properties.property import ListProperty

from eon_timer.util.properties.settings import Settings


@component()
class CustomTimerModel(Settings):
    phases = ListProperty()

    @property
    def group(self) -> str:
        return 'custom'
