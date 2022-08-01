from enum import Enum
from typing import Optional

from eon_timer.gui.form_layout import FormLayout
from PySide6.QtCore import Qt, Slot
from PySide6.QtWidgets import *

from ..field_set import FieldSet
from ..scroll_widget import ScrollWidget

from ...util.constants import INT_MAX, INT_MIN
from ..util import set_class


class TimerMode(Enum):
    STANDARD = 'Standard'
    C_GEAR = 'C-Gear'
    ENTRALINK = 'Entralink'
    ENTRALINK_PLUS = 'Entralink+'

    def __str__(self) -> str:
        return self.value


class TimerWidget(QWidget):
    class Field(Enum):
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

        def __str__(self) -> str:
            return self.value

    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        QWidget.set_class = set_class
        field_sets: dict[self.Field, FieldSet] = {}
        self.__field_sets = field_sets
        # ----- root_layout -----
        root_layout = FormLayout(self)
        root_layout.set_alignment(Qt.AlignTop)
        root_layout.set_content_margins(10, 0, 10, 10)
        # ----- mode -----
        field = QComboBox()
        self.__add_field(root_layout, field, self.Field.MODE)
        field.currentIndexChanged.connect(self.__on_mode_changed)
        for mode in TimerMode:
            field.addItem(mode.value, mode)
        # ----- scroll_area -----
        scroll_widget = ScrollWidget()
        scroll_widget.setSizePolicy(
            QSizePolicy.Expanding,
            QSizePolicy.Expanding)
        root_layout.add_row(scroll_widget)
        # ----- target_delay -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.TARGET_DELAY)
        field.setRange(0, INT_MAX)
        # ----- target_second -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.TARGET_SECOND)
        field.setRange(0, INT_MAX)
        # ----- target_advances -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.TARGET_ADVANCES)
        field.setRange(0, INT_MAX)
        # ----- calibration -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.CALIBRATION)
        field.setRange(INT_MIN, INT_MAX)
        # ----- entralink_calibration -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.ENTRALINK_CALIBRATION)
        field.setRange(INT_MIN, INT_MAX)
        # ----- frame_calibration -----
        field = QSpinBox()
        self.__add_field(scroll_widget.layout, field,
                         self.Field.FRAME_CALIBRATION)
        field.setRange(INT_MIN, INT_MAX)
        # ----- delay_hit -----
        field = QSpinBox()
        self.__add_field(root_layout, field,
                         self.Field.DELAY_HIT)
        # ----- second_hit -----
        field = QSpinBox()
        self.__add_field(root_layout, field,
                         self.Field.SECOND_HIT)
        # ----- advances_hit -----
        field = QSpinBox()
        self.__add_field(root_layout, field,
                         self.Field.ADVANCES_HIT)

    def __add_field(self, layout: FormLayout, widget: QWidget, field: Field) -> None:
        field_set = layout.add_row(widget, field.value)
        self.__field_sets[field] = field_set

    def __set_visible(self, field: Field, visible: bool) -> None:
        field_set = self.__field_sets.get(field, None)
        if field_set is not None:
            field_set.visible = visible

    @Slot()
    def __on_mode_changed(self, index: int) -> None:
        mode = tuple(TimerMode)[index]
        self.__set_visible(self.Field.TARGET_DELAY,
                           mode != TimerMode.STANDARD)
        self.__set_visible(self.Field.TARGET_ADVANCES,
                           mode == TimerMode.ENTRALINK_PLUS)
        self.__set_visible(self.Field.ENTRALINK_CALIBRATION,
                           mode == TimerMode.ENTRALINK or mode == TimerMode.ENTRALINK_PLUS)
        self.__set_visible(self.Field.FRAME_CALIBRATION,
                           mode == TimerMode.ENTRALINK_PLUS)
        self.__set_visible(self.Field.DELAY_HIT,
                           mode != TimerMode.STANDARD)
        self.__set_visible(self.Field.SECOND_HIT,
                           mode != TimerMode.C_GEAR)
        self.__set_visible(self.Field.ADVANCES_HIT,
                           mode == TimerMode.ENTRALINK or mode == TimerMode.ENTRALINK_PLUS)
