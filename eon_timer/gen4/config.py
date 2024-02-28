from pydantic import BaseModel, Field


class Gen4Config(BaseModel):
    target_delay: int = Field(default=600)
    target_second: int = Field(default=50)
    calibrated_delay: int = Field(default=500)
    calibrated_second: int = Field(default=14)
