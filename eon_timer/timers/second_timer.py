from eon_timer import timers
from eon_timer.util.injector import component


@component()
class SecondTimer:
    def create(self,
               target_second: int,
               calibration: float) -> list[float]:
        return [timers.to_minimum_length(target_second * 1000 + calibration + 200)]

    def calibrate(self,
                  target_second: int,
                  second_hit: int) -> float:
        if second_hit < target_second:
            return ((target_second - second_hit) * 1000) - 500
        elif second_hit > target_second:
            return ((target_second - second_hit) * 1000) + 500
        else:
            return 0
