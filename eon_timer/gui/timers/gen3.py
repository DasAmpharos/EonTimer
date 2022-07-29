from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from ...util.constants import INT_MAX, INT_MIN
from ..util import set_class


class TimerWidget(QWidget):
    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.__init_components()

    def __init_components(self) -> None:
        QWidget.set_class = set_class
        root_layout = QVBoxLayout(parent=self)
        root_layout.setContentsMargins(10, 0, 10, 0)
        root_layout.setSpacing(10)
        # group
        group = QGroupBox()
        group.set_class(['themeable-panel', 'themeable-border'])
        group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        root_layout.addWidget(group)
        # group_layout
        group_layout = QVBoxLayout(group)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.setSpacing(0)
        # form
        form = QWidget()
        form.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        group_layout.addWidget(form, 0, Qt.AlignTop)
        # form_layout
        form_layout = QGridLayout(parent=form)
        form_layout.setSpacing(10)
        # ----- pre_timer -----
        field = QSpinBox()
        label = QLabel('Pre-Timer')
        label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        form_layout.addWidget(label, 0, 0, Qt.AlignRight)
        form_layout.addWidget(field, 0, 1)
        # ----- target_frame -----
        field = QSpinBox()
        label = QLabel('Target Frame')
        label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        form_layout.addWidget(label, 1, 0, Qt.AlignRight)
        form_layout.addWidget(field, 1, 1)
        # ----- calibration -----
        field = QSpinBox()
        label = QLabel('Calibration')
        # field.setRange(INT_MIN, INT_MAX)
        label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        form_layout.addWidget(label, 2, 0, Qt.AlignRight)
        form_layout.addWidget(field, 2, 1)
