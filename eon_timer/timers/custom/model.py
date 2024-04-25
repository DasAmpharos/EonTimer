from typing import override

from PySide6.QtCore import QSettings

from eon_timer.util.injector import component
from eon_timer.util.properties.settings import Settings
from .custom_phase import CustomPhase


@component()
class CustomTimerModel(Settings):
    def __init__(self, settings: QSettings):
        self.__phases: list[CustomPhase] = []
        Settings.__init__(self, settings)

    @property
    @override
    def group(self) -> str:
        return 'custom'

    @override
    def _deserialize(self):
        for i in range(self.settings.beginReadArray(self.group)):
            self.settings.setArrayIndex(i)
            unit = self.settings.value('unit', CustomPhase.Unit.MILLISECONDS, str)
            value = self.settings.value('value', 0, int)
            calibration = self.settings.value('calibration', 0.0, float)
            self.__phases.append(CustomPhase(unit, value, calibration))
        self.settings.endArray()

    @override
    def _serialize(self):
        self.settings.beginWriteArray(self.group, len(self.__phases))
        for i, phase in enumerate(self.__phases):
            self.settings.setArrayIndex(i)
            self.settings.setValue('unit', phase.unit.get())
            self.settings.setValue('value', phase.target.get())
            self.settings.setValue('calibration', phase.calibration.get())
        self.settings.endArray()

    @property
    def phases(self) -> list[CustomPhase]:
        return list(self.__phases)

    def append(self, phase: CustomPhase):
        self.__phases.append(phase)
        self.settings_changed.emit()

    def remove(self, phase: CustomPhase):
        self.__phases.remove(phase)
        self.settings_changed.emit()
