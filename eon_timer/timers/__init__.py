from typing import Final

from .calibrator import Calibrator
from .delay_timer import DelayTimer
from .enhanced_entralink_timer import EnhancedEntralinkTimer
from .entralink_timer import EntralinkTimer
from .frame_timer import FrameTimer
from .second_timer import SecondTimer

MINIMUM_LENGTH: Final[int] = 14000


def to_minimum_length(value: int | float) -> int | float:
    while value < MINIMUM_LENGTH:
        value += 60000
    return value
