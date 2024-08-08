from enum import StrEnum
from typing import Final, Self

from PySide6.QtCore import QSettings

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
        self.hit: Final = IntProperty(None, transient=True)

    @classmethod
    def read(cls, settings: QSettings) -> Self:
        phase = cls()
        phase.unit.read(settings, 'unit')
        phase.target.read(settings, 'value')
        phase.calibration.read(settings, 'calibration')
        return phase

    @classmethod
    def write(cls, settings: QSettings, value: Self):
        settings.setValue('unit', value.unit.get())
        settings.setValue('value', value.target.get())
        settings.setValue('calibration', value.calibration.get())

    def __repr__(self):
        return str({
            'unit': self.unit.get(),
            'value': self.target.get(),
            'calibration': self.calibration.get()
        })

    def dispose(self):
        self.unit.dispose()
        self.target.dispose()
        self.calibration.dispose()
        self.hit.dispose()
