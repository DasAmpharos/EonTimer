from typing import Final

from eon_timer.util.properties.property import BoolProperty, Property
from eon_timer.util.properties.settings import Settings


class UpdateSettingsModel(Settings):
    check_on_startup: Final = BoolProperty(True)
    acknowledged: Final = Property('')

    @property
    def group(self) -> str:
        return 'updates'
