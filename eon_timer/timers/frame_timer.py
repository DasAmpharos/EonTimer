from typing import Final

from eon_timer.util.injector import component
from .calibrator import Calibrator


@component()
class FrameTimer:
    def __init__(self, calibrator: Calibrator):
        self.calibrator: Final[Calibrator] = calibrator

    def create(self,
               pre_timer: int,
               target_frame: int,
               calibration: int) -> list[int]:
        return [pre_timer, self.calibrator.to_milliseconds(target_frame) + calibration]

    def calibrate(self,
                  target_frame: int,
                  frame_hit: int) -> int:
        return self.calibrator.to_milliseconds(target_frame - frame_hit)
