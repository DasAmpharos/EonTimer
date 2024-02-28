from typing import Optional, Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QSizePolicy, QSpinBox, QWidget

from eon_timer import util
from eon_timer.gen4 import Gen4Config
from eon_timer.util import FormLayout, FormWidget
from eon_timer.util.constants import INT_MAX, INT_MIN


class Gen4Widget(FormWidget):
    class Field(FormWidget.Field):
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        CALIBRATED_DELAY = 'Calibrated Delay'
        CALIBRATED_SECOND = 'Calibrated Second'
        DELAY_HIT = 'Delay Hit'

    def __init__(self,
                 config: Gen4Config,
                 parent: Optional[QWidget] = None) -> None:
        self.__config: Final[Gen4Config] = config
        super().__init__(parent)

    def _init_components(self) -> None:
        self.setObjectName('gen4Widget')
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- form_group -----
        form_group = QGroupBox()
        self.layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        util.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        field.setValue(self.__config.target_delay)
        self.add_field(self.Field.TARGET_DELAY, field, layout=form_layout)
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        field.setValue(self.__config.target_second)
        self.add_field(self.Field.TARGET_SECOND, field, layout=form_layout)
        # ----- calibrated_delay -----
        field = QSpinBox()
        field.setRange(INT_MIN, INT_MAX)
        field.setValue(self.__config.calibrated_delay)
        self.add_field(self.Field.CALIBRATED_DELAY, field, layout=form_layout)
        # ----- calibrated_second -----
        field = QSpinBox()
        field.setRange(0, INT_MAX)
        field.setValue(self.__config.calibrated_second)
        self.add_field(self.Field.CALIBRATED_SECOND, field, layout=form_layout)
        # ----- delay_hit -----
        field = QSpinBox()
        self.add_field(self.Field.DELAY_HIT, field)
        field.setRange(0, INT_MAX)
