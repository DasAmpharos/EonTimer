import functools

import PySide6.QtGui
from PySide6.QtWidgets import *
from PySide6.QtCore import Qt

from .app_widget import AppWidget


class AppWindow(QMainWindow):
    def __init__(self, parent: QWidget | None = None):
        super().__init__(parent)
        self.app_widget = AppWidget(self)
        self.__init_components()

    def __init_components(self):
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

        # ----- menu -----
        menu = QMenu()
        menu_bar = QMenuBar()
        menu_bar.addMenu(menu)

    def closeEvent(self, event: PySide6.QtGui.QCloseEvent) -> None:
        super().closeEvent(event)
