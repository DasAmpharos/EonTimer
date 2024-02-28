from pydantic import BaseModel

from eon_timer.settings.action.config import ActionConfig
from eon_timer.settings.timer.config import TimerConfig


class SettingsConfig(BaseModel):
    action: ActionConfig
    timer: TimerConfig
