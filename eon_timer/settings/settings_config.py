from pydantic import BaseModel

from .action_config import ActionSettingsConfig
from .timer_config import TimerSettingsConfig


class SettingsConfig(BaseModel):
    action: ActionSettingsConfig
    timer: TimerSettingsConfig
