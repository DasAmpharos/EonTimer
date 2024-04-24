from enum import StrEnum
from typing import Final

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.properties.property import IntProperty, Property


class CustomPhase:
    class Unit(EnhancedEnum, StrEnum):
        MILLISECONDS = 'ms'
        ADVANCES = 'Advances'
        HEX = 'Seed (Hex)'

    def __init__(self,
                 value: int = 0,
                 unit: Unit = Unit.MILLISECONDS):
        self.unit: Final[Property[CustomPhase.Unit]] = Property(unit)
        self.target: Final[IntProperty] = IntProperty(value)
        self.calibration: Final[IntProperty] = IntProperty(0)
        self.hit: Final[IntProperty] = IntProperty(0, transient=True)

    def dispose(self):
        self.target.dispose()
        self.calibration.dispose()
        self.unit.dispose()
        self.hit.dispose()
