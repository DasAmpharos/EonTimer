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
        # ----- form_group -----
        form_group = QGroupBox()
        root_layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        form_group.set_class(['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- target_delay -----
        field = QSpinBox()
        form_layout.add_row(field, 'Target Delay')
        field.setRange(0, INT_MAX)
        # ----- target_second -----
        field = QSpinBox()
        form_layout.add_row(field, 'Target Second')
        field.setRange(0, INT_MAX)
        # ----- calibrated_delay -----
        field = QSpinBox()
        form_layout.add_row(field, 'Calibrated Delay')
        field.setRange(INT_MIN, INT_MAX)
        # ----- calibrated_second -----
        field = QSpinBox()
        form_layout.add_row(field, 'Calibrated Second')
        field.setRange(0, INT_MAX)
        # ----- delay_hit -----
        field = QSpinBox()
        root_layout.add_row(field, 'Delay Hit')
        field.setRange(0, INT_MAX)
