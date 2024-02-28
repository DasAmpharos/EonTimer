from pydantic import BaseModel, Field

from eon_timer.settings.timer.console import Console


class TimerConfig(BaseModel):
    console: Console = Field(default=Console.NDS_SLOT1)
    precision_calibration: bool = Field(default=False)
    refresh_interval: int = Field(default=8)
