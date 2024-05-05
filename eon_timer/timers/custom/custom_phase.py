from enum import StrEnum
from typing import Final

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.properties.property import EnumProperty, FloatProperty, IntProperty


class CustomPhase:
    class Unit(EnhancedEnum, StrEnum):
        MILLISECONDS = 'ms'
        ADVANCES = 'Advances'
        HEX = 'Seed (Hex)'

    def __init__(self,
                 unit: Unit = Unit.MILLISECONDS,
                 value: int = 0,
                 calibration: float = 0.0):
        self.unit: Final = EnumProperty(unit)
        self.target: Final = IntProperty(value)
        self.calibration: Final = FloatProperty(calibration)
        self.hit: Final = IntProperty(0, transient=True)

    def dispose(self):
        self.unit.dispose()
        self.target.dispose()
        self.calibration.dispose()
        self.hit.dispose()
