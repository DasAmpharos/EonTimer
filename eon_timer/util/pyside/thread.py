from typing import Callable, Final, Optional

from PySide6.QtCore import QThread, QObject


class DelegatingQThread(QThread):
    def __init__(self,
                 callable: Callable,
                 parent: Optional[QObject] = None):
        super().__init__(parent)
        self.__callable: Final = callable

    def run(self):
        self.__callable()
