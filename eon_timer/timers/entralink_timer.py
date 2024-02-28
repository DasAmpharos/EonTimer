from typing import Final

from eon_timer.timers.delay_timer import DelayTimer


class EntralinkTimer:
    def __init__(self, delay_timer: DelayTimer):
        self.__delay_timer: Final[DelayTimer] = delay_timer

    def create(self,
               target_delay: int,
               target_second: int,
               calibration: int,
               entralink_calibration: int) -> list[int]:
        durations = self.__delay_timer.create(target_delay, target_second, calibration)
        durations[0] += 250
        durations[1] -= entralink_calibration
        return durations

    def calibrate(self,
                  target_delay: int,
                  delay_hit: int) -> int:
        return self.__delay_timer.calibrate(target_delay, delay_hit)
