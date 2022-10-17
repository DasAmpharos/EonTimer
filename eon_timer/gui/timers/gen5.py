from PySide6.QtCore import Qt, Slot
from PySide6.QtWidgets import QComboBox, QSizePolicy, QSpinBox

from eon_timer.constants import INT_MAX, INT_MIN
from eon_timer.gui.form_widget import FormWidget
from eon_timer.gui.scroll_widget import ScrollWidget
from eon_timer.util import StrEnum


class TimerWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        # target field names
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        TARGET_ADVANCES = 'Target Advances'
        # calibration field names
        CALIBRATION = 'Calibration'
        ENTRALINK_CALIBRATION = 'Entralink Calibration'
        FRAME_CALIBRATION = 'Frame Calibration'
        # result field names
        DELAY_HIT = 'Delay Hit'
        SECOND_HIT = 'Second Hit'
        ADVANCES_HIT = 'Advances Hit'

    class Mode(StrEnum):
        STANDARD = 'Standard'
        C_GEAR = 'C-Gear'
        ENTRALINK = 'Entralink'
        ENTRALINK_PLUS = 'Entralink+'

    def _init_components(self) -> None:
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = QComboBox()
        self._add_field(self.Field.MODE, field)
        field.currentIndexChanged.connect(self.__on_mode_changed)
        for mode in self.Mode:
            field.addItem(str(mode), mode)
        # ----- scroll_area -----
        scroll_widget = ScrollWidget()
        scroll_widget.setSizePolicy(
            QSizePolicy.Expanding,
            QSizePolicy.Expanding)
        self.layout.add_row(scroll_widget)
        # ----- target_delay -----
        field = QSpinBox()
        self._add_field(self.Field.TARGET_DELAY, field,
                        layout=scroll_widget.layout)
        field.setRange(0, INT_MAX)
        # ----- target_second -----
        field = QSpinBox()
        self._add_field(self.Field.TARGET_SECOND, field,
                        layout=scroll_widget.layout)
        field.setRange(0, INT_MAX)
        # ----- target_advances -----
        field = QSpinBox()
        self._add_field(self.Field.TARGET_ADVANCES, field,
                        layout=scroll_widget.layout)
        field.setRange(0, INT_MAX)
        # ----- calibration -----
        field = QSpinBox()
        self._add_field(self.Field.CALIBRATION, field,
                        layout=scroll_widget.layout)
        field.setRange(INT_MIN, INT_MAX)
        # ----- entralink_calibration -----
        field = QSpinBox()
        self._add_field(self.Field.ENTRALINK_CALIBRATION, field,
                        layout=scroll_widget.layout)
        field.setRange(INT_MIN, INT_MAX)
        # ----- frame_calibration -----
        field = QSpinBox()
        self._add_field(self.Field.FRAME_CALIBRATION, field,
                        layout=scroll_widget.layout)
        field.setRange(INT_MIN, INT_MAX)
        # ----- delay_hit -----
        field = QSpinBox()
        self._add_field(self.Field.DELAY_HIT, field)
        field.setRange(0, INT_MAX)
        # ----- second_hit -----
        field = QSpinBox()
        self._add_field(self.Field.SECOND_HIT, field)
        field.setRange(0, INT_MAX)
        # ----- advances_hit -----
        field = QSpinBox()
        self._add_field(self.Field.ADVANCES_HIT, field)
        field.setRange(0, INT_MAX)

    @Slot()
    def __on_mode_changed(self, index: int) -> None:
        mode = tuple(self.Mode)[index]
        self._set_visible(self.Field.TARGET_DELAY,
                          mode != self.Mode.STANDARD)
        self._set_visible(self.Field.TARGET_ADVANCES,
                          mode == self.Mode.ENTRALINK_PLUS)
        self._set_visible(self.Field.ENTRALINK_CALIBRATION,
                          mode == self.Mode.ENTRALINK or mode == self.Mode.ENTRALINK_PLUS)
        self._set_visible(self.Field.FRAME_CALIBRATION,
                          mode == self.Mode.ENTRALINK_PLUS)
        self._set_visible(self.Field.DELAY_HIT,
                          mode != self.Mode.STANDARD)
        self._set_visible(self.Field.SECOND_HIT,
                          mode != self.Mode.C_GEAR)
        self._set_visible(self.Field.ADVANCES_HIT,
                          mode == self.Mode.ENTRALINK or mode == self.Mode.ENTRALINK_PLUS)
