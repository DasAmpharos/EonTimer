from typing import Final, override

from eon_timer.util.injector import component
from eon_timer.util.properties.property import ListProperty
from eon_timer.util.properties.settings import Settings
from .custom_phase import CustomPhase


@component()
class CustomTimerModel(Settings):
    phases: Final = ListProperty([], CustomPhase,
                                 element_reader=CustomPhase.read,
                                 element_writer=CustomPhase.write)

    @property
    @override
    def group(self) -> str:
        return 'custom'

    def append(self, phase: CustomPhase):
        self.phases.append(phase)
        self.settings_changed.emit()

    def remove(self, phase: CustomPhase):
        self.phases.remove(phase)
        self.settings_changed.emit()

    def reset(self):
        for phase in self.phases:
            phase.dispose()
        self.phases.clear()
        self.settings_changed.emit()
