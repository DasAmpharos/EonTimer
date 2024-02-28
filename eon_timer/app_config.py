from pydantic import BaseModel

from .gen3 import Gen3Config
from .gen4 import Gen4Config
from .gen5 import Gen5Config
from .config import SettingsConfig


class AppConfig(BaseModel):
    version: str
    gen3: Gen3Config
    gen4: Gen4Config
    gen5: Gen5Config
    settings: SettingsConfig
