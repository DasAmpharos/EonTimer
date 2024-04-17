import importlib.resources
import sys
import zipfile
from string import Template
from typing import Final

import sass

from eon_timer import resources
from eon_timer.app_window import AppWindow
from eon_timer.settings.theme.model import ThemeSettingsModel, Theme
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class ThemeManager:
    def __init__(self,
                 app_window: AppWindow,
                 theme_settings: ThemeSettingsModel):
        self.app_window: Final[AppWindow] = app_window
        self.theme_settings: Final[ThemeSettingsModel] = theme_settings
        theme_settings.theme.on_change(self.__on_theme_changed)
        event = PropertyChangeEvent(None, theme_settings.theme.get())
        self.__on_theme_changed(event)

    def __on_theme_changed(self, event: PropertyChangeEvent[Theme]):
        match event.new_value:
            case Theme.DEFAULT:
                self.__load_default_style()
            case Theme.SYSTEM:
                self.app_window.setStyleSheet('')

    def __load_default_style(self):
        # style sheet
        caret_up = resources.get_filepath('eon_timer.resources.images', 'caret-up.png')
        caret_down = resources.get_filepath('eon_timer.resources.images', 'caret-down.png')
        caret_up_disabled = resources.get_filepath('eon_timer.resources.images', 'caret-up-disabled.png')
        caret_down_disabled = resources.get_filepath('eon_timer.resources.images', 'caret-down-disabled.png')
        background = resources.get_filepath('eon_timer.resources.images', 'background.png')
        stylesheet = importlib.resources.read_text('eon_timer.resources', 'main.scss')

        template = Template(stylesheet)
        stylesheet = template.safe_substitute(
            caret_up=self.__normalize_file(caret_up),
            caret_down=self.__normalize_file(caret_down),
            caret_up_disabled=self.__normalize_file(caret_up_disabled),
            caret_down_disabled=self.__normalize_file(caret_down_disabled),
            background_image=self.__normalize_file(background)
        )
        stylesheet = sass.compile(string=stylesheet)
        self.app_window.setStyleSheet(stylesheet)

    @staticmethod
    def __normalize_file(filename: str) -> str:
        if hasattr(sys, 'getwindowsversion'):
            return filename.replace('\\', '/')
        return filename
