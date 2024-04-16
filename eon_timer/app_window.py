import importlib.resources
import sys
from string import Template
from typing import Final

import sass
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer import resources
from eon_timer.app_widget import AppWidget
from eon_timer.settings.theme.model import ThemeSettingsModel, Theme
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class AppWindow(QMainWindow):
    def __init__(self,
                 app_widget: AppWidget,
                 theme_settings: ThemeSettingsModel) -> None:
        super().__init__()
        self.app_widget: Final[AppWidget] = app_widget
        self.theme_settings: Final[ThemeSettingsModel] = theme_settings
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('EonTimer - v3.0.0')
        self.setWindowFlags(Qt.Window |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint |
                            Qt.WindowMinimizeButtonHint |
                            Qt.WindowMaximizeButtonHint)
        self.setCentralWidget(self.app_widget)
        self.setMinimumSize(525, 395)
        # load theme
        self.theme_settings.theme.on_change(self.__on_theme_changed)
        event = PropertyChangeEvent(None, self.theme_settings.theme.get())
        self.__on_theme_changed(event)

    @staticmethod
    def __normalize_file(filename: str) -> str:
        if hasattr(sys, 'getwindowsversion'):
            return filename.replace('\\', '/')
        return filename

    def __on_theme_changed(self, event: PropertyChangeEvent[Theme]):
        match event.new_value:
            case Theme.DEFAULT:
                self.__load_default_style()
            case Theme.SYSTEM:
                self.setStyleSheet('')

    def __load_default_style(self):
        # style sheet
        caret_up = resources.get_filepath('eon_timer.resources.icons', 'caret-up.png')
        caret_down = resources.get_filepath('eon_timer.resources.icons', 'caret-down.png')
        caret_up_disabled = resources.get_filepath('eon_timer.resources.icons', 'caret-up-disabled.png')
        caret_down_disabled = resources.get_filepath('eon_timer.resources.icons', 'caret-down-disabled.png')
        background_image = resources.get_filepath('eon_timer.resources.images', 'default_background.png')
        stylesheet = importlib.resources.read_text('eon_timer.resources.styles', 'main.scss')

        template = Template(stylesheet)
        stylesheet = template.safe_substitute(
            caret_up=self.__normalize_file(caret_up),
            caret_down=self.__normalize_file(caret_down),
            caret_up_disabled=self.__normalize_file(caret_up_disabled),
            caret_down_disabled=self.__normalize_file(caret_down_disabled),
            background_image=self.__normalize_file(background_image)
        )
        stylesheet = sass.compile(string=stylesheet)
        self.setStyleSheet(stylesheet)

