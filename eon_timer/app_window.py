from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer import app_version
from eon_timer.app_widget import AppWidget
from eon_timer.util.injector import component
from eon_timer.util.pyside.name_service import NameService


@component()
class AppWindow(QMainWindow):
    def __init__(self,
                 name_service: NameService,
                 app_widget: AppWidget) -> None:
        super().__init__()
        name_service.set_name(self, 'appWindow')
        self.setWindowTitle(f'EonTimer - {app_version.get_version()}')
        self.setWindowFlags(Qt.Window |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint |
                            Qt.WindowMinimizeButtonHint |
                            Qt.WindowMaximizeButtonHint)
        self.setCentralWidget(app_widget)
        self.setMinimumSize(600, 450)
        self.resize(600, 450)
