import functools
from string import Template
from typing import Optional

import pkg_resources
import PySide6.QtGui
import sass
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from .app_widget import AppWidget


class AppWindow(QMainWindow):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.app_widget = AppWidget(parent=self)
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('EonTimer - v3.0.0')
        self.setWindowFlags(functools.reduce(
            lambda f1, f2: f1 | f2,
            [
                Qt.Window,
                Qt.WindowTitleHint,
                Qt.CustomizeWindowHint,
                Qt.WindowCloseButtonHint,
                Qt.WindowMinimizeButtonHint
            ]))
        self.setCentralWidget(self.app_widget)
        self.setFixedSize(525, 395)

        # style sheet
        styleSheet = pkg_resources.resource_string(
            'eon_timer.resources.styles', 'main.scss')
        background_image_name = pkg_resources.resource_filename(
            'eon_timer.resources.images', 'default_background.png')

        template = Template(styleSheet.decode())
        styleSheet = template.safe_substitute(
            background_image=background_image_name)
        styleSheet = sass.compile(string=styleSheet)
        self.setStyleSheet(styleSheet)

        # ----- menu -----
        menu = QMenu()
        menu_bar = QMenuBar()
        menu_bar.addMenu(menu)

    def closeEvent(self, event: PySide6.QtGui.QCloseEvent) -> None:
        print('closing...')
        super().closeEvent(event)
