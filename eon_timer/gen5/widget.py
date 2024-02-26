from typing import Optional, Callable

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QComboBox, QSizePolicy, QSpinBox, QWidget

from eon_timer import util
from eon_timer.gen5 import Gen5Config
from eon_timer.util.constants import INT_MAX, INT_MIN
from eon_timer.util.form_widget import FormWidget
from eon_timer.util.scroll_widget import ScrollWidget


class Gen5Widget(FormWidget):
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

    def __init__(self,
                 config: Gen5Config,
                 parent: Optional[QWidget] = None) -> None:
        self.config = config
        super().__init__(parent)

    def _init_components(self) -> None:
        # ----- layout -----
        self.setObjectName('gen5Widget')
        self.layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        mode_field = QComboBox()
        util.add_items(mode_field, Gen5Config.Mode, self.config.mode)
        self.add_field(self.Field.MODE, mode_field)
        # ----- scroll_area -----
        scroll_area = ScrollWidget()
        scroll_area.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding)
        self.layout.add_row(scroll_area)

        # ----- form_widget -----
        form = FormWidget()
        form.layout.set_spacing(10)
        form.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        mode_field.currentIndexChanged.connect(self.__on_mode_changed(form))
        scroll_area.layout.addWidget(form, 0, Qt.AlignmentFlag.AlignTop)
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        form.add_field(self.Field.TARGET_DELAY, field)
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        form.add_field(self.Field.TARGET_SECOND, field)
        # ----- target_advances -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        form.add_field(self.Field.TARGET_ADVANCES, field)
        # ----- calibration -----
        field = QSpinBox()
        field.setRange(INT_MIN, INT_MAX)
        form.add_field(self.Field.CALIBRATION, field)
        # ----- entralink_calibration -----
        field = QSpinBox()
        field.setRange(INT_MIN, INT_MAX)
        form.add_field(self.Field.ENTRALINK_CALIBRATION, field)
        # ----- frame_calibration -----
        field = QSpinBox()
        field.setRange(INT_MIN, INT_MAX)
        form.add_field(self.Field.FRAME_CALIBRATION, field)
        # ----- delay_hit -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.DELAY_HIT, field)
        # ----- second_hit -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.SECOND_HIT, field)
        # ----- advances_hit -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        self.add_field(self.Field.ADVANCES_HIT, field)
        # update field visibility
        idx = Gen5Config.Mode.index_of(self.config.mode)
        self.__on_mode_changed(form)(idx)

    @classmethod
    def __on_mode_changed(cls, form: FormWidget) -> Callable[[int], None]:
        def implementation(index: int) -> None:
            mode = Gen5Config.Mode.get(index)
            form.set_visible(cls.Field.TARGET_DELAY,
                             mode != Gen5Config.Mode.STANDARD)
            form.set_visible(cls.Field.TARGET_ADVANCES,
                             mode == Gen5Config.Mode.ENTRALINK_PLUS)
            form.set_visible(cls.Field.ENTRALINK_CALIBRATION,
                             mode == Gen5Config.Mode.ENTRALINK or
                             mode == Gen5Config.Mode.ENTRALINK_PLUS)
            form.set_visible(cls.Field.FRAME_CALIBRATION,
                             mode == Gen5Config.Mode.ENTRALINK_PLUS)
            form.set_visible(cls.Field.DELAY_HIT,
                             mode == Gen5Config.Mode.STANDARD)
            form.set_visible(cls.Field.SECOND_HIT,
                             mode == Gen5Config.Mode.C_GEAR)
            form.set_visible(cls.Field.ADVANCES_HIT,
                             mode == Gen5Config.Mode.ENTRALINK or
                             mode == Gen5Config.Mode.ENTRALINK_PLUS)

        return implementation
