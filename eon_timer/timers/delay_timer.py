from typing import Final

from eon_timer import timers
from eon_timer.util.injector import component
from .calibrator import Calibrator
from .second_timer import SecondTimer


@component()
class DelayTimer:
    CLOSE_THRESHOLD: Final[int] = 167
    UPDATE_FACTOR: Final[float] = 1.0
    CLOSE_UPDATE_FACTOR: Final[float] = 0.75

    def __init__(self,
                 calibrator: Calibrator,
                 second_timer: SecondTimer):
        self.calibrator: Final[Calibrator] = calibrator
        self.second_timer: Final[SecondTimer] = second_timer

    def create(self, target_delay: int, target_second: int, calibration: float) -> list[float]:
        phases = [
            self.__create_phase1(target_delay, target_second, calibration),
            self.__create_phase2(target_delay, calibration)
        ]
        return phases

    def __create_phase1(self, target_delay: int, target_second: int, calibration: float) -> float:
        return timers.to_minimum_length(
            self.second_timer.create(target_second, calibration)[0] -
            self.calibrator.to_milliseconds(target_delay)
        )

    def __create_phase2(self, target_delay: int, calibration: float) -> float:
        return self.calibrator.to_milliseconds(target_delay) - calibration

    def calibrate(self, target_delay: int, delay_hit: int) -> float:
        delta = self.calibrator.to_milliseconds(delay_hit) - self.calibrator.to_milliseconds(target_delay)
        if abs(delta) <= self.CLOSE_THRESHOLD:
            return self.CLOSE_UPDATE_FACTOR * delta
        else:
            return self.UPDATE_FACTOR * delta
