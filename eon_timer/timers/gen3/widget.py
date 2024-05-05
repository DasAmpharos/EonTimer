import functools
import logging
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QGroupBox, QPushButton, QSizePolicy, QSpinBox, QDoubleSpinBox

from eon_timer.app_state import AppState
from eon_timer.timers import FrameTimer, VariableFrameTimer
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget, FormLayout
from eon_timer.util.pyside.name_service import NameService
from .model import Gen3Mode, Gen3Model


@component()
class Gen3TimerWidget(FormWidget):
    timer_changed: Final = Signal()

    class Field(FormWidget.Field):
        MODE = 'Mode'
        PRE_TIMER = 'Pre-Timer'
        TARGET_FRAME = 'Target Frame'
        CALIBRATION = 'Calibration'
        SET_TARGET_FRAME = 'Set Target Frame'
        FRAME_HIT = 'Frame Hit'

    def __init__(self,
                 state: AppState,
                 model: Gen3Model,
                 frame_timer: FrameTimer,
                 variable_frame_timer: VariableFrameTimer,
                 name_service: NameService) -> None:
        super().__init__(name_service)
        self.state: Final = state
        self.model: Final = model
        self.frame_timer: Final = frame_timer
        self.variable_frame_timer: Final = variable_frame_timer
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.name_service.set_name(self, 'gen3TimerWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(Gen3Mode)
        bindings.bind_enum_combobox(field, self.model.mode)
        self.model.mode.on_change(self.__on_mode_changed)
        self.add_field(self.Field.MODE, field, name='gen3Mode')
        # ----- form_group -----
        form_group = QGroupBox()
        self.name_service.set_name(form_group, 'gen3FormGroup')
        self._layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        pyside.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- pre_timer -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.pre_timer)
        self.add_field(self.Field.PRE_TIMER, field, layout=form_layout, name='gen3PreTimer')
        # ----- target_frame -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_frame)
        self.add_field(self.Field.TARGET_FRAME, field, layout=form_layout, name='gen3TargetFrame')
        # ----- calibration -----
        field = QDoubleSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_float_spinbox(field, self.model.calibration)
        self.add_field(self.Field.CALIBRATION, field, layout=form_layout, name='gen3Calibration')
        # ----- set_target_frame_btn -----
        field = QPushButton(self.Field.SET_TARGET_FRAME.value)
        self.name_service.set_name(field, 'gen3SetTargetFrameButton')
        field_set = self.add_field(self.Field.SET_TARGET_FRAME, field,
                                   layout=form_layout,
                                   with_label=False)
        field.pressed.connect(self.__on_set_target_frame)
        field_set.enabled = False
        # ----- frame_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.frame_hit)
        self.add_field(self.Field.FRAME_HIT, field, name='gen3FrameHit')
        # update field visibility
        self.__on_mode_changed()

    def __init_listeners(self):
        def field_changed(field: Gen3TimerWidget.Field,
                          event: PropertyChangeEvent) -> None:
            if not self.state.running:
                logging.info(f'> INFO: Gen3Widget#{field}: {event.new_value}')
                self.timer_changed.emit()

        self.model.mode.on_change(functools.partial(field_changed, self.Field.MODE))
        self.model.pre_timer.on_change(functools.partial(field_changed, self.Field.PRE_TIMER))
        self.model.target_frame.on_change(functools.partial(field_changed, self.Field.TARGET_FRAME))
        self.model.calibration.on_change(functools.partial(field_changed, self.Field.CALIBRATION))

    def __on_mode_changed(self, event: PropertyChangeEvent[Gen3Mode] | None = None) -> None:
        mode = event.new_value if event else self.model.mode.get()
        self.set_visible(self.Field.SET_TARGET_FRAME,
                         mode == Gen3Mode.VARIABLE_TARGET)

    def __on_set_target_frame(self) -> None:
        phase = self.frame_timer.create_phase(
            self.model.target_frame.get(),
            self.model.calibration.get()
        )
        self.state.set_phase(1, phase)
        self.set_disabled(self.Field.TARGET_FRAME, True)
        self.set_disabled(self.Field.SET_TARGET_FRAME, True)

    def create_phases(self) -> list[float]:
        match self.model.mode.get():
            case Gen3Mode.STANDARD:
                return self.frame_timer.create(
                    self.model.pre_timer.get(),
                    self.model.target_frame.get(),
                    self.model.calibration.get()
                )
            case Gen3Mode.VARIABLE_TARGET:
                return self.variable_frame_timer.create(
                    self.model.pre_timer.get()
                )

    def calibrate(self):
        if self.model.frame_hit.get() > 0:
            offset = self.frame_timer.calibrate(
                self.model.target_frame.get(),
                self.model.frame_hit.get()
            )
            self.model.calibration.add(offset)
            self.model.frame_hit.set(0)

    def setDisabled(self, disabled: bool):
        self.set_disabled(self.Field.MODE, disabled)
        self.set_disabled(self.Field.PRE_TIMER, disabled)
        self.set_disabled(self.Field.CALIBRATION, disabled)
        self.set_disabled(self.Field.FRAME_HIT, disabled)

        mode = self.model.mode.get()
        self.set_disabled(self.Field.TARGET_FRAME, self.state.running and mode == Gen3Mode.STANDARD)
        self.set_enabled(self.Field.SET_TARGET_FRAME, self.state.running and mode == Gen3Mode.VARIABLE_TARGET)
