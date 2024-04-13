import sys
from string import Template
from typing import Final

import pkg_resources
import sass
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from .app_widget import AppWidget
from .util.injector import component


@component()
class AppWindow(QMainWindow):
    def __init__(self,
                 app_widget: AppWidget) -> None:
        super().__init__()
        self.app_widget: Final[AppWidget] = app_widget
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('EonTimer - v3.0.0')
        self.setWindowFlags(Qt.Window |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint |
                            Qt.WindowMinimizeButtonHint)
        self.setCentralWidget(self.app_widget)
        self.setMinimumSize(525, 395)

        # style sheet
        stylesheet = pkg_resources.resource_string(
            'eon_timer.resources.styles', 'main.scss')
        caret_up = pkg_resources.resource_filename(
            'eon_timer.resources.icons', 'caret-up.png')
        caret_down = pkg_resources.resource_filename(
            'eon_timer.resources.icons', 'caret-down.png')
        caret_up_disabled = pkg_resources.resource_filename(
            'eon_timer.resources.icons', 'caret-up-disabled.png')
        caret_down_disabled = pkg_resources.resource_filename(
            'eon_timer.resources.icons', 'caret-down-disabled.png')
        background_image = pkg_resources.resource_filename(
            'eon_timer.resources.images', 'default_background.png')

        # template = Template(stylesheet.decode())
        # stylesheet = template.safe_substitute(
        #     caret_up=self.__normalize_file(caret_up),
        #     caret_down=self.__normalize_file(caret_down),
        #     caret_up_disabled=self.__normalize_file(caret_up_disabled),
        #     caret_down_disabled=self.__normalize_file(caret_down_disabled),
        #     background_image=self.__normalize_file(background_image)
        # )
        # stylesheet = sass.compile(string=stylesheet)
        # self.setStyleSheet(stylesheet)
        # with open('main.css', 'w') as file:
        #     file.write(stylesheet)

        # ----- menu -----
        menu = QMenu()
        menu_bar = QMenuBar()
        menu_bar.addMenu(menu)

    @staticmethod
    def __normalize_file(filename: str) -> str:
        if hasattr(sys, 'getwindowsversion'):
            return filename.replace('\\', '/')
        return filename
