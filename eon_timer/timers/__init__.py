from typing import Final

from .calibrator import Calibrator
from .delay_timer import DelayTimer
from .entralink_timer import EnhancedEntralinkTimer, EntralinkTimer
from .frame_timer import FrameTimer, VariableFrameTimer
from .second_timer import SecondTimer

MINIMUM_LENGTH: Final = 14000


def to_minimum_length(value: int | float) -> int | float:
    while value < MINIMUM_LENGTH:
        value += 60000
    return value
