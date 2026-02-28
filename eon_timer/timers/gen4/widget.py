from typing import Final, override

from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import const, strings
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import BlankBehavior, IntInputField

from .model import Gen4Model
from .timer import Gen4Timer


class Gen4TimerWidget(TimerWidget[Gen4Model, Gen4Timer], FormWidget):
    class Field(FormWidget.Field):
        CALIBRATED_DELAY = 'Calibrated Delay'
        CALIBRATED_SECOND = 'Calibrated Second'
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        DELAY_HIT = 'Delay Hit'

    def __init__(self, model: Gen4Model, timer: Gen4Timer, name_service: NameService):
        self.delay_hit_field: Final = IntInputField(
            min_val=0, max_val=const.INT_MAX,
            blank_behavior=BlankBehavior.BLANK,
            placeholder='Enter hit delay',
            tooltip='The delay you actually hit — enter this after each run to calibrate',
        )
        FormWidget.__init__(self, name_service)
        TimerWidget.__init__(self, model, timer)

    @override
    @log_method_calls()
    def _init_components(self) -> None:
        self.name_service.set_name(self, 'gen4TimerWidget')
        # ----- form_group -----
        _, form_layout = self._add_form_group('gen4FormGroup')
        # ----- calibrated_delay -----
        self.add_bound_field(self.Field.CALIBRATED_DELAY,
            IntInputField(min_val=const.INT_MIN, max_val=const.INT_MAX, tooltip='Your console-specific delay calibration (auto-updated after each run)'),
            self.model.calibrated_delay, layout=form_layout, name='gen4CalibratedDelay')
        # ----- calibrated_second -----
        self.add_bound_field(self.Field.CALIBRATED_SECOND,
            IntInputField(min_val=0, max_val=const.INT_MAX, tooltip='Your console-specific second calibration (auto-updated after each run)'),
            self.model.calibrated_second, layout=form_layout, name='gen4CalibratedSecond')
        # ----- target_delay -----
        self.add_bound_field(self.Field.TARGET_DELAY,
            IntInputField(min_val=0, max_val=const.INT_MAX, tooltip='The delay value from your target seed'),
            self.model.target_delay, layout=form_layout, name='gen4TargetDelay')
        # ----- target_second -----
        self.add_bound_field(self.Field.TARGET_SECOND,
            IntInputField(min_val=0, max_val=const.INT_MAX, tooltip='The second value from your target seed'),
            self.model.target_second, layout=form_layout, name='gen4TimerSecond')
        # ----- delay_hit -----
        self.add_bound_field(self.Field.DELAY_HIT, self.delay_hit_field, self.model.delay_hit, name='gen4DelayHit')

    @override
    def _init_listeners(self):
        self._register_field_listeners(
            [
                (self.model.target_delay, self.Field.TARGET_DELAY),
                (self.model.target_second, self.Field.TARGET_SECOND),
                (self.model.calibrated_delay, self.Field.CALIBRATED_DELAY),
                (self.model.calibrated_second, self.Field.CALIBRATED_SECOND),
                (self.model.delay_hit, self.Field.DELAY_HIT),
            ]
        )

    @override
    def calibrate(self):
        if strings.strip_to_none(self.delay_hit_field.text()) is not None:
            super().calibrate()
            self.delay_hit_field.setText('')

    def can_calibrate(self) -> bool:
        return strings.strip_to_none(self.delay_hit_field.text()) is not None
