from typing import Optional
from PySide6.QtWidgets import QWidget

class TimerDisplayWidget(QWidget):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        pass