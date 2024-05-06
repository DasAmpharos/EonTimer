from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer import app
from eon_timer.app_widget import AppWidget
from eon_timer.util.injector import component
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.updates import UpdateChecker


@component()
class AppWindow(QMainWindow):
    def __init__(self,
                 app_widget: AppWidget,
                 update_checker: UpdateChecker,
                 name_service: NameService) -> None:
        super().__init__()
        self.update_checker: Final = update_checker
        name_service.set_name(self, 'appWindow')
        self.setWindowTitle(f'EonTimer - {app.get_version()}')
        self.setWindowFlags(Qt.Window |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint |
                            Qt.WindowMinimizeButtonHint |
                            Qt.WindowMaximizeButtonHint)
        self.setCentralWidget(app_widget)
        self.setMinimumSize(600, 450)
        self.resize(600, 450)

    def show(self):
        super().show()
        print(self.update_checker.check_for_updates())
