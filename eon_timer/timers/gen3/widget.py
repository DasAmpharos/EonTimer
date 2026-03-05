from typing import Final, override

from PySide6.QtWidgets import QPushButton

from eon_timer.app_state import AppState
from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import const, strings
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import BlankBehavior, FloatInputField, IntInputField

from .model import Gen3Mode, Gen3Model
from .timer import Gen3Timer


class Gen3TimerWidget(TimerWidget[Gen3Model, Gen3Timer], FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        PRE_TIMER = 'Pre-Timer'
        TARGET_FRAME = 'Target Frame'
        CALIBRATION = 'Calibration'
        SET_TARGET_FRAME = 'Set Target Frame'
        FRAME_HIT = 'Frame Hit'

    def __init__(self, state: AppState, model: Gen3Model, timer: Gen3Timer, name_service: NameService) -> None:
        self.state: Final = state
        self.frame_hit_field: Final = IntInputField(
            min_val=0,
            max_val=const.INT_MAX,
            blank_behavior=BlankBehavior.BLANK,
            placeholder='Enter frame hit',
            tooltip='The frame you actually landed on — enter this after each run to calibrate',
        )
        FormWidget.__init__(self, name_service)
        TimerWidget.__init__(self, model, timer)

    @override
    @log_method_calls()
    def _init_components(self) -> None:
        self.name_service.set_name(self, 'gen3TimerWidget')
        # ----- mode -----
        field = EnumComboBox(Gen3Mode)
        bindings.bind_enum_combobox(field, self.model.mode)
        self.model.mode.on_change(self.__on_mode_changed)
        self.add_field(self.Field.MODE, field, name='gen3Mode')
        # ----- form_group -----
        _, form_layout = self._add_form_group('gen3FormGroup')
        # ----- pre_timer -----
        self.add_bound_field(
            self.Field.PRE_TIMER,
            IntInputField(
                min_val=0,
                max_val=const.INT_MAX,
                tooltip='Milliseconds to wait before the first phase (typically 1000–3000)',
            ),
            self.model.pre_timer,
            layout=form_layout,
            name='gen3PreTimer',
        )
        # ----- target_frame -----
        self.add_bound_field(
            self.Field.TARGET_FRAME,
            IntInputField(min_val=0, max_val=const.INT_MAX, tooltip='The frame number you want to land on'),
            self.model.target_frame,
            layout=form_layout,
            name='gen3TargetFrame',
        )
        # ----- calibration -----
        self.add_bound_field(
            self.Field.CALIBRATION,
            FloatInputField(
                min_val=const.INT_MIN,
                max_val=const.INT_MAX,
                tooltip='Calibration offset in milliseconds (auto-updated after each run)',
            ),
            self.model.calibration,
            layout=form_layout,
            name='gen3Calibration',
        )
        # ----- set_target_frame_btn -----
        field = QPushButton(self.Field.SET_TARGET_FRAME.value)
        self.name_service.set_name(field, 'gen3SetTargetFrameButton')
        field_set = self.add_field(self.Field.SET_TARGET_FRAME, field, layout=form_layout, with_label=False)
        field.pressed.connect(self.__on_set_target_frame)
        field_set.enabled = False
        # ----- frame_hit -----
        self.add_bound_field(self.Field.FRAME_HIT, self.frame_hit_field, self.model.frame_hit, name='gen3FrameHit')
        # update field visibility
        self.__on_mode_changed()

    @override
    def _init_listeners(self):
        self._register_field_listeners(
            [
                (self.model.mode, self.Field.MODE),
                (self.model.pre_timer, self.Field.PRE_TIMER),
                (self.model.target_frame, self.Field.TARGET_FRAME),
                (self.model.calibration, self.Field.CALIBRATION),
                (self.model.frame_hit, self.Field.FRAME_HIT),
            ]
        )

    @override
    def calibrate(self):
        frame_hit = strings.strip_to_none(self.frame_hit_field.text())
        if frame_hit is not None:
            super().calibrate()
            self.frame_hit_field.setText('')

    def can_calibrate(self) -> bool:
        return strings.strip_to_none(self.frame_hit_field.text()) is not None

    @override
    def setDisabled(self, disabled: bool):
        self.set_disabled(self.Field.MODE, disabled)
        self.set_disabled(self.Field.PRE_TIMER, disabled)
        self.set_disabled(self.Field.CALIBRATION, disabled)
        self.set_disabled(self.Field.FRAME_HIT, disabled)

        mode = self.model.mode.get()
        self.set_disabled(self.Field.TARGET_FRAME, self.state.running and mode == Gen3Mode.STANDARD)
        self.set_enabled(self.Field.SET_TARGET_FRAME, self.state.running and mode == Gen3Mode.VARIABLE_TARGET)

    def __on_mode_changed(self, event: PropertyChangeEvent[Gen3Mode] | None = None) -> None:
        mode = event.new_value if event else self.model.mode.get()
        self.set_visible(self.Field.SET_TARGET_FRAME, mode == Gen3Mode.VARIABLE_TARGET)

    def __on_set_target_frame(self) -> None:
        phase = self.timer.frame_timer.create_phase(self.model.target_frame.get(), self.model.calibration.get())
        self.state.set_phase(1, phase)
        self.set_disabled(self.Field.TARGET_FRAME, True)
        self.set_disabled(self.Field.SET_TARGET_FRAME, True)
