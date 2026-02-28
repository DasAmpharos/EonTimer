from typing import Final, override

from eon_timer.timers import FrameTimer, VariableFrameTimer
from eon_timer.timers.timer import Timer

from .model import Gen3Mode, Gen3Model


class Gen3Timer(Timer[Gen3Model]):
    def __init__(self, frame_timer: FrameTimer, variable_frame_timer: VariableFrameTimer):
        self.frame_timer: Final = frame_timer
        self.variable_frame_timer: Final = variable_frame_timer

    @override
    def create(self, model: Gen3Model) -> list[float]:
        match model.mode.get():
            case Gen3Mode.STANDARD:
                return self.frame_timer.create(model.pre_timer.get(), model.target_frame.get(), model.calibration.get())
            case Gen3Mode.VARIABLE_TARGET:
                return self.variable_frame_timer.create(model.pre_timer.get())
            case _ as mode:
                raise ValueError(f'Unsupported Gen3Mode: {mode}')

    @override
    def calibrate(self, model: Gen3Model):
        if model.frame_hit.get() is not None:
            offset = self.frame_timer.calibrate(model.target_frame.get(), model.frame_hit.get())
            model.calibration.add(offset)
            model.frame_hit.set(None)
