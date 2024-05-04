from PySide6.QtCore import QSettings

from eon_timer.theme.theme_manager import ThemeManager
from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.settings import Settings


@component()
class ThemeSettingsModel(Settings):
    theme = Property(ThemeManager.DEFAULT_THEME)
    element_name_tooltip = Property(False)

    def __init__(self,
                 settings: QSettings,
                 theme_manager: ThemeManager):
        Settings.__init__(self, settings)
        if self.theme.get() not in theme_manager.list_theme_names():
            self.theme.set(ThemeManager.DEFAULT_THEME)

    @property
    def group(self) -> str:
        return 'theme'
