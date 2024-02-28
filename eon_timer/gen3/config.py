from enum import StrEnum

from pydantic import BaseModel, Field

from eon_timer.util.enum import EnhancedEnum


class Gen3Config(BaseModel):
    class Mode(EnhancedEnum, StrEnum):
        STANDARD = 'Standard'
        VARIABLE_TARGET = 'Variable Target'

    mode: Mode = Field(default=Mode.STANDARD)
    pre_timer: int = Field(default=5000)
    target_frame: int = Field(default=1000)
    calibration: int = Field(default=0)
