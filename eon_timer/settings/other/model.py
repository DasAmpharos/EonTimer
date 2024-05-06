from typing import Final, override

from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


@component()
class OtherSettingsModel(Settings):
    check_for_updates: Final = Property(True)

    @property
    @override
    def group(self) -> str:
        return 'other'
