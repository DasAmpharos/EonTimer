from eon_timer.theme.theme_manager import ThemeManager
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


@component()
class ThemeSettingsModel(Settings):
    theme = Property(ThemeManager.DEFAULT_THEME)

    @property
    def group(self) -> str:
        return 'theme'
