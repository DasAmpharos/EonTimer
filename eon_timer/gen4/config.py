import pydantic
from pydantic import BaseModel


class Gen4Config(BaseModel):
    target_delay: int = pydantic.Field(default=0)
    target_second: int = pydantic.Field(default=0)
    calibrated_delay: int = pydantic.Field(default=0)
    calibrated_second: int = pydantic.Field(default=0)
