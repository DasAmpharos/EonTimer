from typing import Optional

from eon_timer.gui.form_layout import FormLayout
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from ...util.constants import INT_MAX, INT_MIN
from ..util import set_class


class TimerWidget(QWidget):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        QWidget.set_class = set_class
        # ----- root_layout -----
        root_layout = FormLayout(self)
        root_layout.set_alignment(Qt.AlignTop)
        root_layout.set_content_margins(10, 0, 10, 10)
        # ----- mode -----
        field = QComboBox()
        root_layout.add_row(field, 'Mode')
        # ----- form_group -----
        form_group = QGroupBox()
        root_layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        form_group.set_class(['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- pre_timer -----
        field = QSpinBox()
        form_layout.add_row(field, 'Pre-Timer')
        field.setRange(0, INT_MAX)
        # ----- target_frame -----
        field = QSpinBox()
        form_layout.add_row(field, 'Target Frame')
        field.setRange(0, INT_MAX)
        # ----- calibration -----
        field = QSpinBox()
        form_layout.add_row(field, 'Calibration')
        field.setRange(INT_MIN, INT_MAX)
        # ----- frame_hit -----
        field = QSpinBox()
        root_layout.add_row(field, 'Frame Hit')
        field.setRange(0, INT_MAX)
