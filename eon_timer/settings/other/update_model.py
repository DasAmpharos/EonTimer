from typing import Final

from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property, BoolProperty
from eon_timer.util.properties.settings import Settings


@component()
class UpdateSettingsModel(Settings):
    check_on_startup: Final = BoolProperty(True)
    acknowledged: Final = Property('')

    @property
    def group(self) -> str:
        return 'updates'
