import functools
import logging
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QSizePolicy, QSpinBox, QGroupBox

from eon_timer.app_state import AppState
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox, ScrollWidget
from eon_timer.util.pyside.form import FormWidget, FormLayout
from .model import Gen5Model, Gen5Mode
from ..timers import Calibrator, DelayTimer, SecondTimer, EntralinkTimer, EnhancedEntralinkTimer


@component()
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

    timer_changed: Final[Signal] = Signal()

    def __init__(self,
                 state: AppState,
                 model: Gen5Model,
                 calibrator: Calibrator,
                 delay_timer: DelayTimer,
                 second_timer: SecondTimer,
                 entralink_timer: EntralinkTimer,
                 enhanced_entralink_timer: EnhancedEntralinkTimer) -> None:
        super().__init__(None)
        self.state: Final[AppState] = state
        self.model: Final[Gen5Model] = model
        self.calibrator: Final[Calibrator] = calibrator
        self.delay_timer: Final[DelayTimer] = delay_timer
        self.second_timer: Final[SecondTimer] = second_timer
        self.entralink_timer: Final[EntralinkTimer] = entralink_timer
        self.enhanced_entralink_timer: Final[EnhancedEntralinkTimer] = enhanced_entralink_timer
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.setObjectName('gen5Widget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        mode_field = EnumComboBox(Gen5Mode)
        bindings.bind_combobox(mode_field, self.model.mode)
        self.add_field(self.Field.MODE, mode_field)
        # ----- form_group -----
        form_group = QGroupBox()
        self._layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        pyside.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
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
        # ----- target_advances -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_advances)
        self.add_field(self.Field.TARGET_ADVANCES, field, layout=form_layout)
        # ----- calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibration)
        self.add_field(self.Field.CALIBRATION, field, layout=form_layout)
        # ----- entralink_calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.entralink_calibration)
        self.add_field(self.Field.ENTRALINK_CALIBRATION, field, layout=form_layout)
        # ----- frame_calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.frame_calibration)
        self.add_field(self.Field.FRAME_CALIBRATION, field, layout=form_layout)
        # ----- delay_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.delay_hit)
        self.add_field(self.Field.DELAY_HIT, field)
        # ----- second_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.second_hit)
        self.add_field(self.Field.SECOND_HIT, field)
        # ----- advances_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.advances_hit)
        self.add_field(self.Field.ADVANCES_HIT, field)
        # update field visibility
        self.model.mode.on_change(self.__on_mode_changed)
        event = PropertyChangeEvent(None, self.model.mode.get())
        self.__on_mode_changed(event)

    def __init_listeners(self):
        def field_changed(field: Gen5Widget.Field,
                          event: PropertyChangeEvent) -> None:
            logging.info(f'> INFO: Gen5Widget#{field}: {event.new_value}')
            self.timer_changed.emit()

        # mode
        handler = functools.partial(field_changed, self.Field.MODE)
        self.model.mode.on_change(handler)
        # target_delay
        handler = functools.partial(field_changed, self.Field.TARGET_DELAY)
        self.model.target_delay.on_change(handler)
        # target_second
        handler = functools.partial(field_changed, self.Field.TARGET_SECOND)
        self.model.target_second.on_change(handler)
        # target_advances
        handler = functools.partial(field_changed, self.Field.TARGET_ADVANCES)
        self.model.target_advances.on_change(handler)
        # calibration
        handler = functools.partial(field_changed, self.Field.CALIBRATION)
        self.model.calibration.on_change(handler)
        # entralink_calibration
        handler = functools.partial(field_changed, self.Field.ENTRALINK_CALIBRATION)
        self.model.entralink_calibration.on_change(handler)
        # frame_calibration
        handler = functools.partial(field_changed, self.Field.FRAME_CALIBRATION)
        self.model.frame_calibration.on_change(handler)

    def __on_mode_changed(self, event: PropertyChangeEvent[Gen5Mode]):
        self.set_visible(self.Field.TARGET_DELAY,
                         event.new_value != Gen5Mode.STANDARD)
        self.set_visible(self.Field.TARGET_ADVANCES,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.ENTRALINK_CALIBRATION,
                         event.new_value == Gen5Mode.ENTRALINK or
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.FRAME_CALIBRATION,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.DELAY_HIT,
                         event.new_value != Gen5Mode.STANDARD)
        self.set_visible(self.Field.SECOND_HIT,
                         event.new_value != Gen5Mode.C_GEAR)
        self.set_visible(self.Field.ADVANCES_HIT,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)

    def create_phases(self) -> list[int]:
        return self.enhanced_entralink_timer.create(
            self.model.target_delay.get(),
            self.model.target_second.get(),
            self.model.target_advances.get(),
            self.model.calibration.get(),
            self.model.entralink_calibration.get(),
            self.model.frame_calibration.get()
        )
