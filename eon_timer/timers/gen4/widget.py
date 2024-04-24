import functools
import logging
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QGroupBox, QSizePolicy, QSpinBox

from eon_timer.timers import Calibrator, DelayTimer
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside.form import FormLayout, FormWidget
from .model import Gen4Model


@component()
class Gen4TimerWidget(FormWidget):
    timer_changed: Final[Signal] = Signal()

    class Field(FormWidget.Field):
        CALIBRATED_DELAY = 'Calibrated Delay'
        CALIBRATED_SECOND = 'Calibrated Second'
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        DELAY_HIT = 'Delay Hit'

    def __init__(self,
                 model: Gen4Model,
                 calibrator: Calibrator,
                 delay_timer: DelayTimer) -> None:
        super().__init__(None)
        self.model: Final[Gen4Model] = model
        self.calibrator: Final[Calibrator] = calibrator
        self.delay_timer: Final[DelayTimer] = delay_timer
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.setObjectName('gen4Widget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- form_group -----
        form_group = QGroupBox()
        self._layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        pyside.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- calibrated_delay -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibrated_delay)
        self.add_field(self.Field.CALIBRATED_DELAY, field, layout=form_layout)
        # ----- calibrated_second -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibrated_second)
        self.add_field(self.Field.CALIBRATED_SECOND, field, layout=form_layout)
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_delay)
        self.add_field(self.Field.TARGET_DELAY, field, layout=form_layout)
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_second)
        self.add_field(self.Field.TARGET_SECOND, field, layout=form_layout)
        # ----- delay_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.delay_hit)
        self.add_field(self.Field.DELAY_HIT, field)

    def __init_listeners(self):
        def field_changed(field: Gen4TimerWidget.Field,
                          event: PropertyChangeEvent) -> None:
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

    def create_phases(self) -> list[int]:
        return self.delay_timer.create(
            self.model.target_delay.get(),
            self.model.target_second.get(),
            self.get_calibration()
        )

    def calibrate(self):
        if self.model.delay_hit.get() > 0:
            calibration = self.calibrator.to_delays(
                self.delay_timer.calibrate(
                    self.model.target_delay.get(),
                    self.model.delay_hit.get()
                )
            )
            self.model.calibrated_delay.add(calibration)
            self.model.delay_hit.set(0)

    def get_calibration(self) -> int:
        return self.calibrator.create_calibration(
            self.model.calibrated_delay.get(),
            self.model.calibrated_second.get()
        )
