from typing import Optional

from pydantic import BaseModel, Field

from eon_timer.settings.action.action_color import ActionColor
from eon_timer.settings.action.action_mode import ActionMode
from eon_timer.settings.action.action_sound import (ActionSound)


class ActionConfig(BaseModel):
    mode: ActionMode = Field(default=ActionMode.AV)
    sound: ActionSound = Field(default=ActionSound.BEEP)
    color: ActionColor = Field(default_factory=lambda: ActionColor(r=0, g=0, b=255))
    custom_sound: Optional[str] = Field(default=None)
    interval: int = Field(default=500)
    count: int = Field(default=6)
