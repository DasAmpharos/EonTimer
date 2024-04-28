from typing import Final

from eon_timer.app_window import AppWindow
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.settings.theme.model import ThemeSettingsModel
from eon_timer.theme.theme_manager import ThemeManager
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class ThemeEngine:
    def __init__(self,
                 app_window: AppWindow,
                 settings_dialog: SettingsDialog,
                 theme_settings: ThemeSettingsModel,
                 theme_manager: ThemeManager):
        self.app_window: Final[AppWindow] = app_window
        self.settings_dialog: Final[SettingsDialog] = settings_dialog
        self.theme_settings: Final[ThemeSettingsModel] = theme_settings
        self.theme_manager: Final[ThemeManager] = theme_manager
        theme_settings.theme.on_change(self.__apply_theme)
        self.__apply_theme()

    def __apply_theme(self, _: PropertyChangeEvent[str] | None = None):
        theme_name = self.theme_settings.theme.get()
        theme = self.theme_manager.get_theme(theme_name)
        self.__apply_stylesheet(theme.stylesheet)

    def __apply_stylesheet(self, stylesheet: str):
        self.app_window.setStyleSheet(stylesheet)
        self.settings_dialog.setStyleSheet(stylesheet)
