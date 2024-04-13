from typing import Optional, Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QFrame,
                               QScrollArea,
                               QSizePolicy,
                               QWidget, QVBoxLayout)

from eon_timer.util import pyside


class ScrollWidget(QScrollArea):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__scroll_pane: Final[QWidget] = QWidget()
        self.layout: Final[QVBoxLayout] = QVBoxLayout(self.__scroll_pane)
        self.__init_components()

    def __init_components(self) -> None:
        # ----- scroll_pane -----
        pyside.set_class(self.__scroll_pane, ['themeable-panel'])
        self.__scroll_pane.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- scroll_pane_layout -----
        self.layout.setContentsMargins(0, 0, 0, 0)
        self.layout.setSpacing(10)
        # ----- self -----
        self.setWidget(self.__scroll_pane)
        self.setFrameShape(QFrame.Shape.NoFrame)
        self.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        self.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        pyside.set_class(self, ['themeable-panel', 'themeable-border'])
        self.setWidgetResizable(True)
