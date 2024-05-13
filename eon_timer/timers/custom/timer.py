from typing import Final

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.timer import Timer
from eon_timer.util.injector import component
from .custom_phase import CustomPhase
from .model import CustomTimerModel


@component()
class CustomTimer(Timer[CustomTimerModel]):
    def __init__(self, calibrator: Calibrator):
        self.calibrator: Final = calibrator

    def create(self, model: CustomTimerModel) -> list[float]:
        phases = []
        for phase in model.phases:
            unit = phase.unit.get()
            value = phase.target.get()
            if unit == CustomPhase.Unit.ADVANCES or unit == CustomPhase.Unit.HEX:
                value = self.calibrator.to_milliseconds(value)
            value += phase.calibration.get()
            phases.append(value)
        return phases

    def calibrate(self, model: CustomTimerModel):
        pass
