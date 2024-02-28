from typing import Final

from eon_timer.timers import util
from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.second_timer import SecondTimer


class DelayTimer:
    CLOSE_THRESHOLD: Final[int] = 167
    UPDATE_FACTOR: Final[float] = 1.0
    CLOSE_UPDATE_FACTOR: Final[float] = 0.75

    def __init__(self,
                 second_timer: SecondTimer,
                 calibrator: Calibrator):
        self.__second_timer: Final[SecondTimer] = second_timer
        self.__calibrator: Final[Calibrator] = calibrator

    def create(self, target_delay: int, target_second: int, calibration: int) -> list[int]:
        return [
            self.__create_duration1(target_delay, target_second, calibration),
            self.__create_duration2(target_delay, calibration)
        ]

    def __create_duration1(self, target_delay: int, target_second: int, calibration: int) -> int:
        return util.to_minimum_length(
            self.__second_timer.create(target_second, calibration)[0] -
            self.__calibrator.to_milliseconds(target_delay)
        )

    def __create_duration2(self, target_delay: int, calibration: int) -> int:
        return self.__calibrator.to_milliseconds(target_delay) - calibration

    def calibrate(self, target_delay: int, delay_hit: int) -> int:
        delta = self.__calibrator.to_milliseconds(delay_hit) - self.__calibrator.to_milliseconds(target_delay)
        if abs(delta) <= self.CLOSE_THRESHOLD:
            return int(self.CLOSE_UPDATE_FACTOR * delta)
        else:
            return int(self.UPDATE_FACTOR * delta)
