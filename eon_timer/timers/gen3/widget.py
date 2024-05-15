import functools
import logging
from typing import Final, override

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QDoubleSpinBox, QGroupBox, QPushButton, QSizePolicy, QSpinBox

from eon_timer.app_state import AppState
from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormLayout, FormWidget
from eon_timer.util.pyside.name_service import NameService
from .model import Gen3Mode, Gen3Model
from .timer import Gen3Timer


@component()
class Gen3TimerWidget(TimerWidget[Gen3Model, Gen3Timer], FormWidget):
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
                 timer: Gen3Timer,
                 name_service: NameService) -> None:
        self.state: Final = state
        FormWidget.__init__(self, name_service)
        TimerWidget.__init__(self, model, timer)

    @override
    @log_method_calls()
    def _init_components(self) -> None:
        self.name_service.set_name(self, 'gen3TimerWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(Gen3Mode)
        self.add_field(self.Field.MODE, field, name='gen3Mode')
        bindings.bind_enum_combobox(field, self.model.mode)
        self.model.mode.on_change(self.__on_mode_changed)
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
        self.add_field(self.Field.PRE_TIMER, field, layout=form_layout, name='gen3PreTimer')
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.pre_timer)
        # ----- target_frame -----
        field = QSpinBox()
        self.add_field(self.Field.TARGET_FRAME, field, layout=form_layout, name='gen3TargetFrame')
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_frame)
        # ----- calibration -----
        field = QDoubleSpinBox()
        self.add_field(self.Field.CALIBRATION, field, layout=form_layout, name='gen3Calibration')
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_float_spinbox(field, self.model.calibration)
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
        self.add_field(self.Field.FRAME_HIT, field, name='gen3FrameHit')
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.frame_hit)
        # update field visibility
        self.__on_mode_changed()

    @override
    def _init_listeners(self):
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

    def setDisabled(self, disabled: bool):
        self.set_disabled(self.Field.MODE, disabled)
        self.set_disabled(self.Field.PRE_TIMER, disabled)
        self.set_disabled(self.Field.CALIBRATION, disabled)
        self.set_disabled(self.Field.FRAME_HIT, disabled)

        mode = self.model.mode.get()
        self.set_disabled(self.Field.TARGET_FRAME, self.state.running and mode == Gen3Mode.STANDARD)
        self.set_enabled(self.Field.SET_TARGET_FRAME, self.state.running and mode == Gen3Mode.VARIABLE_TARGET)
