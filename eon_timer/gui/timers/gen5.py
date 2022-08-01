from typing import Optional

from eon_timer.gui.form_layout import FormLayout
from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from ...util.constants import INT_MAX, INT_MIN
from ..util import set_class


class TimerWidget(QWidget):
    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        QWidget.set_class = set_class
        root_layout = QVBoxLayout(self)
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
        form_layout = FormLayout(form)
        form.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        group_layout.addWidget(form, 0, Qt.AlignTop)
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        form_layout.add_row(field, 'Target Delay')
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        form_layout.add_row(field, 'Target Second')
        # ----- calibrated_delay -----
        field = QSpinBox()
        form_layout.add_row(field, 'Calibrated Delay')
        # ----- calibrated_second -----
        field = QSpinBox()
        form_layout.add_row(field, 'Calibrated Second')
