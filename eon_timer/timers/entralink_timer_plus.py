from typing import Final

from eon_timer.timers.entralink_timer import EntralinkTimer


class EntralinkTimerPlus:
    ENTRALINK_FRAME_RATE: Final[float] = 0.837148929

    def __init__(self, entralink_timer: EntralinkTimer):
        self.__entralink_timer: Final[EntralinkTimer] = entralink_timer

    def create(self,
               target_delay: int,
               target_second: int,
               target_advances: int,
               calibration: int,
               entralink_calibration: int,
               frame_calibration: int) -> list[int]:
        durations = self.__entralink_timer.create(target_delay, target_second, calibration, entralink_calibration)
        durations.append(round(target_advances / self.ENTRALINK_FRAME_RATE) * 1000 + frame_calibration)
        return durations

    def calibrate(self, target_advances: int, advances_hit: int) -> int:
        return int((target_advances - advances_hit) / self.ENTRALINK_FRAME_RATE) * 1000
