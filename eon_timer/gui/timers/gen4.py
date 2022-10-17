from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QSizePolicy, QSpinBox

from eon_timer.constants import INT_MAX, INT_MIN
from eon_timer.gui import util
from eon_timer.gui.form_layout import FormLayout
from eon_timer.gui.form_widget import FormWidget


class TimerWidget(FormWidget):
    class Field(FormWidget.Field):
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        CALIBRATED_DELAY = 'Calibrated Delay'
        CALIBRATED_SECOND = 'Calibrated Second'
        DELAY_HIT = 'Delay Hit'

    def _init_components(self) -> None:
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignTop)
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
        self._add_field(self.Field.TARGET_DELAY, field,
                        layout=form_layout)
        field.setRange(0, INT_MAX)
        # ----- target_second -----
        field = QSpinBox()
        self._add_field(self.Field.TARGET_SECOND, field,
                        layout=form_layout)
        field.setRange(0, INT_MAX)
        # ----- calibrated_delay -----
        field = QSpinBox()
        self._add_field(self.Field.CALIBRATED_DELAY, field,
                        layout=form_layout)
        field.setRange(INT_MIN, INT_MAX)
        # ----- calibrated_second -----
        field = QSpinBox()
        self._add_field(self.Field.CALIBRATED_SECOND, field,
                        layout=form_layout)
        field.setRange(0, INT_MAX)
        # ----- delay_hit -----
        field = QSpinBox()
        self._add_field(self.Field.DELAY_HIT, field)
        field.setRange(0, INT_MAX)
