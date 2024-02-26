from __future__ import annotations

from enum import StrEnum

import pydantic
from pydantic import BaseModel

from eon_timer.util.enum import EnhancedEnum


class Gen5Config(BaseModel):
    class Mode(EnhancedEnum, StrEnum):
        STANDARD = 'Standard'
        C_GEAR = 'C-Gear'
        ENTRALINK = 'Entralink'
        ENTRALINK_PLUS = 'Entralink+'

    mode: Mode = pydantic.Field(default=Mode.STANDARD)
    calibration: int = pydantic.Field(default=-95)
    frame_calibration: int = pydantic.Field(default=0)
    entralink_calibration: int = pydantic.Field(default=256)
    target_delay: int = pydantic.Field(default=1200)
    target_second: int = pydantic.Field(default=50)
    target_advances: int = pydantic.Field(default=100)
