from enum import StrEnum

import pydantic
from pydantic import BaseModel

from eon_timer.util.enum import EnhancedEnum


class Gen3Config(BaseModel):
    class Mode(EnhancedEnum, StrEnum):
        STANDARD = 'Standard'
        VARIABLE_TARGET = 'Variable Target'

    mode: Mode = pydantic.Field(default=Mode.STANDARD)
    pre_timer: int = pydantic.Field(default=5000)
    target_frame: int = pydantic.Field(default=1000)
    calibration: int = pydantic.Field(default=0)
