from __future__ import annotations

from enum import StrEnum

from pydantic import BaseModel, Field

from eon_timer.util.enum import EnhancedEnum


class Gen5Config(BaseModel):
    class Mode(EnhancedEnum, StrEnum):
        STANDARD = 'Standard'
        C_GEAR = 'C-Gear'
        ENTRALINK = 'Entralink'
        ENTRALINK_PLUS = 'Entralink+'

    mode: Mode = Field(default=Mode.STANDARD)
    calibration: int = Field(default=-95)
    frame_calibration: int = Field(default=0)
    entralink_calibration: int = Field(default=256)
    target_delay: int = Field(default=1200)
    target_second: int = Field(default=50)
    target_advances: int = Field(default=100)
