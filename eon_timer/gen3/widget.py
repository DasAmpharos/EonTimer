from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QComboBox, QGroupBox, QPushButton, QSizePolicy,
                               QSpinBox, QWidget)

from eon_timer import util
from eon_timer.util import FormLayout, FormWidget
from eon_timer.util.constants import INT_MAX, INT_MIN
from .config import Gen3Config


class Gen3Widget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        PRE_TIMER = 'Pre-Timer'
        TARGET_FRAME = 'Target Frame'
        CALIBRATION = 'Calibration'
        SET_TARGET_FRAME = 'Set Target Frame'
        FRAME_HIT = 'Frame Hit'

    def __init__(self,
                 config: Gen3Config,
                 parent: Optional[QWidget] = None) -> None:
        self.config = config
        super().__init__(parent)

    def _init_components(self) -> None:
        self.setObjectName('gen3Widget')
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = QComboBox()
        util.add_items(field, Gen3Config.Mode, self.config.mode)
        field.currentIndexChanged.connect(self.__on_mode_changed)
        self.add_field(self.Field.MODE, field)

        # ----- form_group -----
        form_group = QGroupBox()
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        util.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        self.layout.add_row(form_group)
        # ----- pre_timer -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.PRE_TIMER, field,
                       layout=form_layout)
        # ----- target_frame -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.TARGET_FRAME, field,
                       layout=form_layout)
        # ----- calibration -----
        field = QSpinBox()
        field.setRange(INT_MIN, INT_MAX)
        self.add_field(self.Field.CALIBRATION, field,
                       layout=form_layout)
        # ----- set_target_frame_btn -----
        field = QPushButton(self.Field.SET_TARGET_FRAME.value)
        field_set = self.add_field(self.Field.SET_TARGET_FRAME, field,
                                   layout=form_layout, with_label=False)
        field_set.enabled = False
        # ----- frame_hit -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.FRAME_HIT, field)

    def __on_mode_changed(self, index: int) -> None:
        mode = Gen3Config.Mode.get(index)
        self.set_visible(self.Field.SET_TARGET_FRAME,
                         mode == Gen3Config.Mode.VARIABLE_TARGET)
