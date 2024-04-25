import importlib.resources
import sys
from string import Template
from typing import Final

import sass

from eon_timer import resources
from eon_timer.app_window import AppWindow
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.settings.theme.model import ThemeSettingsModel, Theme
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class ThemeManager:
    def __init__(self,
                 app_window: AppWindow,
                 settings_dialog: SettingsDialog,
                 theme_settings: ThemeSettingsModel):
        self.app_window: Final[AppWindow] = app_window
        self.settings_dialog: Final[SettingsDialog] = settings_dialog
        self.theme_settings: Final[ThemeSettingsModel] = theme_settings
        theme_settings.theme.on_change(self.__on_theme_changed)
        event = PropertyChangeEvent(None, theme_settings.theme.get())
        self.__on_theme_changed(event)

    def __on_theme_changed(self, event: PropertyChangeEvent[Theme]):
        match event.new_value:
            case Theme.DEFAULT:
                self.__set_default_style()
            case Theme.SYSTEM:
                self.__set_system_style()

    def __set_default_style(self):
        template = Template(importlib.resources.read_text('eon_timer.resources', 'main.scss'))
        stylesheet = template.safe_substitute(
            caret_up=resources.get_filepath('eon_timer.resources.images', 'caret-up.png', True),
            caret_down=resources.get_filepath('eon_timer.resources.images', 'caret-down.png', True),
            caret_up_disabled=resources.get_filepath('eon_timer.resources.images', 'caret-up-disabled.png', True),
            caret_down_disabled=resources.get_filepath('eon_timer.resources.images', 'caret-down-disabled.png', True),
            background=resources.get_filepath('eon_timer.resources.images', 'background.png', True)
        )
        stylesheet = sass.compile(string=stylesheet)
        self.app_window.setStyleSheet(stylesheet)
        self.settings_dialog.setStyleSheet(stylesheet)

    def __set_system_style(self):
        self.app_window.setStyleSheet('')
        self.settings_dialog.setStyleSheet('')
