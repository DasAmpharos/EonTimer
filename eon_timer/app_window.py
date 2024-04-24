from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer.app_widget import AppWidget
from eon_timer.util.injector import component


@component()
class AppWindow(QMainWindow):
    def __init__(self, app_widget: AppWidget) -> None:
        super().__init__()
        self.setWindowTitle('EonTimer - v3.0.0')
        self.setWindowFlags(Qt.Window |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint |
                            Qt.WindowMinimizeButtonHint |
                            Qt.WindowMaximizeButtonHint)
        self.setCentralWidget(app_widget)
        self.setMinimumSize(545, 410)
        self.resize(545, 410)
