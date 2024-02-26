from pydantic import BaseModel, Field

from ..console import Console


class TimerSettingsConfig(BaseModel):
    console: Console = Field(default=Console.NDS_SLOT1)
    precision_calibration: bool = Field(default=False)
    refresh_interval: int = Field(default=8)
