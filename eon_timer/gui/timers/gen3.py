from typing import Optional

from eon_timer.constants import INT_MAX, INT_MIN
from eon_timer.gui import util
from eon_timer.gui.form_layout import FormLayout
from eon_timer.gui.form_widget import FormWidget
from eon_timer.util import StrEnum
from PySide6.QtCore import Qt, Slot
from PySide6.QtWidgets import (QComboBox, QGroupBox, QPushButton, QSizePolicy,
                               QSpinBox, QWidget)


class TimerWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        PRE_TIMER = 'Pre-Timer'
        TARGET_FRAME = 'Target Frame'
        CALIBRATION = 'Calibration'
        SET_TARGET_FRAME = 'Set Target Frame'
        FRAME_HIT = 'Frame Hit'

    class Mode(StrEnum):
        STANDARD = 'Standard'
        VARIABLE_TARGET = 'Variable Target'

    def __init__(self, parent: Optional[QWidget] = None) -> None:
        self.__mode = self.Mode.STANDARD
        super().__init__(parent)

    def _init_components(self) -> None:
        QWidget.set_class = util.set_class
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = QComboBox()
        self._add_field(self.Field.MODE, field)
        field.currentIndexChanged.connect(self.__update)
        for mode in self.Mode:
            field.addItem(mode.value, mode)
        # ----- form_group -----
        form_group = QGroupBox()
        self.layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        form_group.set_class(['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- pre_timer -----
        field = QSpinBox()
        self._add_field(self.Field.PRE_TIMER, field,
                        layout=form_layout)
        field.setRange(0, INT_MAX)
        # ----- target_frame -----
        field = QSpinBox()
        self._add_field(self.Field.TARGET_FRAME, field,
                        layout=form_layout)
        field.setRange(0, INT_MAX)
        # ----- calibration -----
        field = QSpinBox()
        self._add_field(self.Field.CALIBRATION, field,
                        layout=form_layout)
        field.setRange(INT_MIN, INT_MAX)
        # ----- set_target_frame_btn -----
        field = QPushButton(self.Field.SET_TARGET_FRAME.value)
        field_set = self._add_field(self.Field.SET_TARGET_FRAME, field,
                                    layout=form_layout, with_label=False)
        field_set.enabled = False
        # ----- frame_hit -----
        field = QSpinBox()
        self._add_field(self.Field.FRAME_HIT, field)
        field.setRange(0, INT_MAX)

    @Slot()
    def __update(self) -> None:
        mode = self.field_sets[self.Field.MODE]
        current_mode = mode.field.currentIndex()
        current_mode = tuple(self.Mode)[current_mode]
        self.__mode = current_mode

        self._set_visible(self.Field.SET_TARGET_FRAME,
                          current_mode == self.Mode.VARIABLE_TARGET)
