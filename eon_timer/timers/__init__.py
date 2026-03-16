from typing import Final

from .calibrator import Calibrator
from .delay_timer import DelayTimer
from .entralink_timer import EnhancedEntralinkTimer, EntralinkTimer
from .frame_timer import FrameTimer, VariableFrameTimer
from .second_timer import SecondTimer

MINIMUM_LENGTH: Final = 14000


def to_minimum_length(value: int | float, minimum_length: int | float = MINIMUM_LENGTH) -> int | float:
    while value < minimum_length:
        value += 60000
    return value


def get_minutes_before_target(phases: list[float]) -> int:
    """Compute the number of whole minutes in the total phase duration.

    INFINITY phases are skipped so callers can include variable-length
    phases without short-circuiting the calculation.
    """
    from eon_timer.util.const import INFINITY
    total = 0
    for phase in phases:
        if phase == INFINITY:
            continue
        total += phase
    return int(total // 60_000)
