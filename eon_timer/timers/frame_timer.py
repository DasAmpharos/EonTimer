from typing import Final

from eon_timer.util import const
from eon_timer.util.injector import component
from .calibrator import Calibrator


@component()
class FrameTimer:
    def __init__(self, calibrator: Calibrator):
        self.calibrator: Final = calibrator

    def create(self,
               pre_timer: int,
               target_frame: int,
               calibration: float) -> list[float]:
        return [pre_timer, self.create_phase(target_frame, calibration)]

    def create_phase(self, target_frame: int, calibration: float) -> float:
        return self.calibrator.to_milliseconds(target_frame) + calibration

    def calibrate(self,
                  target_frame: int,
                  frame_hit: int) -> float:
        return self.calibrator.to_milliseconds(target_frame - frame_hit)


@component()
class VariableFrameTimer:
    def __init__(self, frame_timer: FrameTimer):
        self.frame_timer: Final = frame_timer

    def create(self, pre_timer: int) -> list[float]:
        return [pre_timer, const.INFINITY]

    def calibrate(self,
                  target_frame: int,
                  frame_hit: int) -> float:
        return self.frame_timer.calibrate(target_frame, frame_hit)
