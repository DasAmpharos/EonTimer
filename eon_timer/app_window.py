from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer import app
from eon_timer.app_widget import AppWidget
from eon_timer.update_manager import UpdateManager
from eon_timer.util.injector import component
from eon_timer.util.pyside.name_service import NameService


@component()
class AppWindow(QMainWindow):
    def __init__(self,
                 app_widget: AppWidget,
                 update_manager: UpdateManager,
                 name_service: NameService) -> None:
        super().__init__()
        self.update_manager: Final = update_manager
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
        if app.is_bundled() and self.update_manager.check_on_startup:
            self.update_manager.check_for_updates(self)
