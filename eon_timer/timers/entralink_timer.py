from typing import Final

from eon_timer.util.injector import component
from .delay_timer import DelayTimer


@component()
class EntralinkTimer:
    def __init__(self, delay_timer: DelayTimer):
        self.delay_timer: Final = delay_timer

    def create(self,
               target_delay: int,
               target_second: int,
               calibration: float,
               entralink_calibration: float) -> list[float]:
        durations = self.delay_timer.create(target_delay, target_second, calibration)
        durations[0] += 250
        durations[1] -= entralink_calibration
        return durations

    def calibrate(self,
                  target_delay: int,
                  delay_hit: int) -> float:
        return self.delay_timer.calibrate(target_delay, delay_hit)


@component()
class EnhancedEntralinkTimer:
    ENTRALINK_FRAME_RATE: Final = 0.837148929

    def __init__(self, entralink_timer: EntralinkTimer):
        self.entralink_timer: Final = entralink_timer

    def create(self,
               target_delay: int,
               target_second: int,
               target_advances: int,
               calibration: float,
               entralink_calibration: float,
               frame_calibration: int) -> list[float]:
        phases = self.entralink_timer.create(target_delay, target_second, calibration, entralink_calibration)
        phases.append((target_advances / self.ENTRALINK_FRAME_RATE) * 1000 + frame_calibration)
        return phases

    def calibrate(self, target_advances: int, advances_hit: int) -> float:
        return ((target_advances - advances_hit) / self.ENTRALINK_FRAME_RATE) * 1000
