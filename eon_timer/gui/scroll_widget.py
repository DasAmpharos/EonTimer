from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QFrame, QScrollArea, QSizePolicy,
                               QWidget)

from . import util
from .form_layout import FormLayout


class ScrollWidget(QScrollArea):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        # ----- scroll_pane -----
        scroll_pane = QWidget()
        util.set_class(scroll_pane, ['themeable-panel'])
        scroll_pane.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- scroll_pane_layout -----
        scroll_pane_layout = FormLayout(scroll_pane)
        scroll_pane_layout.set_alignment(Qt.AlignTop)
        self.layout = scroll_pane_layout
        # ----- self -----
        self.setFrameShape(QFrame.NoFrame)
        self.setVerticalScrollBarPolicy(Qt.ScrollBarAsNeeded)
        self.setHorizontalScrollBarPolicy(Qt.ScrollBarAsNeeded)
        util.set_class(self, ['themeable-panel', 'themeable-border'])
        self.setWidgetResizable(True)
        self.setWidget(scroll_pane)
