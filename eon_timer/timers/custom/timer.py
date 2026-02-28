from typing import Final, override

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.timer import Timer

from .custom_phase import CustomPhase
from .model import CustomTimerModel


class CustomTimer(Timer[CustomTimerModel]):
    def __init__(self, calibrator: Calibrator):
        self.calibrator: Final = calibrator

    @override
    def create(self, model: CustomTimerModel) -> list[float]:
        phases = []
        for phase in model.phases:
            unit = phase.unit.get()
            value = phase.target.get()
            if unit in (CustomPhase.Unit.ADVANCES, CustomPhase.Unit.HEX):
                value = self.calibrator.to_milliseconds(value)
            value += phase.calibration.get()
            phases.append(value)
        return phases

    @override
    def calibrate(self, model: CustomTimerModel):
        pass
