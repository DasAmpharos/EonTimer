import functools
import logging
from typing import override

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QSizePolicy, QSpinBox

from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside.form import FormLayout, FormWidget
from eon_timer.util.pyside.name_service import NameService
from .model import Gen4Model
from .timer import Gen4Timer


@component()
class Gen4TimerWidget(TimerWidget[Gen4Model, Gen4Timer], FormWidget):
    class Field(FormWidget.Field):
        CALIBRATED_DELAY = 'Calibrated Delay'
        CALIBRATED_SECOND = 'Calibrated Second'
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        DELAY_HIT = 'Delay Hit'

    def __init__(self, model: Gen4Model, timer: Gen4Timer, name_service: NameService):
        FormWidget.__init__(self, name_service)
        TimerWidget.__init__(self, model, timer)

    @override
    def _init_components(self) -> None:
        self.name_service.set_name(self, 'gen4TimerWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- form_group -----
        form_group = QGroupBox()
        self.name_service.set_name(form_group, 'gen4FormGroup')
        self._layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        pyside.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- calibrated_delay -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibrated_delay)
        self.add_field(self.Field.CALIBRATED_DELAY, field, layout=form_layout, name='gen4CalibratedDelay')
        # ----- calibrated_second -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibrated_second)
        self.add_field(self.Field.CALIBRATED_SECOND, field, layout=form_layout, name='gen4CalibratedSecond')
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_delay)
        self.add_field(self.Field.TARGET_DELAY, field, layout=form_layout, name='gen4TargetDelay')
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_second)
        self.add_field(self.Field.TARGET_SECOND, field, layout=form_layout, name='gen4TimerSecond')
        # ----- delay_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.delay_hit)
        self.add_field(self.Field.DELAY_HIT, field, name='gen4DelayHit')

    @override
    def _init_listeners(self):
        def field_changed(field: Gen4TimerWidget.Field,
                          event: PropertyChangeEvent) -> None:
            if not self.resetting:
                logging.info(f'> INFO: Gen4Widget#{field}: {event.new_value}')
                self.timer_changed.emit()

        # target_delay
        handler = functools.partial(field_changed, self.Field.TARGET_DELAY)
        self.model.target_delay.on_change(handler)
        # target_second
        handler = functools.partial(field_changed, self.Field.TARGET_SECOND)
        self.model.target_second.on_change(handler)
        # target_frame
        handler = functools.partial(field_changed, self.Field.CALIBRATED_DELAY)
        self.model.calibrated_delay.on_change(handler)
        # calibration
        handler = functools.partial(field_changed, self.Field.CALIBRATED_SECOND)
        self.model.calibrated_second.on_change(handler)
