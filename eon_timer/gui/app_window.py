import functools
from typing import Optional

import PySide6.QtGui
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from app_widget import AppWidget


class AppWindow(QMainWindow):
    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
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

        # ----- menu -----
        menu = QMenu()
        menu_bar = QMenuBar()
        menu_bar.addMenu(menu)

    def closeEvent(self, event: PySide6.QtGui.QCloseEvent) -> None:
        print('closing...')
        super().closeEvent(event)
