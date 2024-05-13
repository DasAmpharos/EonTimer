from typing import Final

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.delay_timer import DelayTimer
from eon_timer.timers.timer import Timer
from eon_timer.util.injector import component
from .model import Gen4Model


@component()
class Gen4Timer(Timer[Gen4Model]):
    def __init__(self,
                 calibrator: Calibrator,
                 delay_timer: DelayTimer):
        self.calibrator: Final = calibrator
        self.delay_timer: Final = delay_timer

    def create(self, model: Gen4Model) -> list[float]:
        return self.delay_timer.create(
            model.target_delay.get(),
            model.target_second.get(),
            self.get_calibration(model)
        )

    def calibrate(self, model: Gen4Model):
        if model.delay_hit.get() > 0:
            calibration = self.calibrator.to_delays(
                self.delay_timer.calibrate(
                    model.target_delay.get(),
                    model.delay_hit.get()
                )
            )
            model.calibrated_delay.add(calibration)
            model.delay_hit.set(0)

    def get_calibration(self, model: Gen4Model) -> float:
        return self.calibrator.create_calibration(
            model.calibrated_delay.get(),
            model.calibrated_second.get()
        )
