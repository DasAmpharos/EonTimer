from typing import Self

from PySide6.QtGui import QColor
from pydantic import BaseModel, Field


class ActionColor(BaseModel):
    r: int
    g: int
    b: int
    a: int = Field(default=255)

    @classmethod
    def from_qcolor(cls, color: QColor) -> Self:
        return cls(r=color.red(), g=color.green(), b=color.blue(), a=color.alpha())

    def to_qcolor(self) -> QColor:
        return QColor.fromRgb(self.r, self.g, self.b, self.a)
