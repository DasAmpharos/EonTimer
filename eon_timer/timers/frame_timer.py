from typing import Final

from eon_timer.timers.calibrator import Calibrator


class FrameTimer:
    def __init__(self, calibrator: Calibrator):
        self.__calibrator: Final[Calibrator] = calibrator

    def create(self,
               pre_timer: int,
               target_frame: int,
               calibration: int) -> list[int]:
        return [pre_timer, self.__calibrator.to_milliseconds(target_frame) + calibration]

    def calibrate(self,
                  target_frame: int,
                  frame_hit: int) -> int:
        return self.__calibrator.to_milliseconds(target_frame - frame_hit)
