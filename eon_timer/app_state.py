from typing import Final

from PySide6.QtCore import QObject, Signal

from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property


@component()
class AppState(QObject):
    phases_changed: Final[Signal] = Signal(list)
    current_phase_changed: Final[Signal] = Signal(int)
    current_phase_elapsed_changed: Final[Signal] = Signal(int)
    minutes_before_target_changed: Final[Signal] = Signal(int)
    next_phase_changed: Final[Signal] = Signal(int)

    def __init__(self):
        super().__init__()
        self.__phases: list[int] = []
        self.__current_phase_index: int = 0
        self.__current_phase_elapsed: int = 0
        self.__resetting = False

    @property
    def phases(self) -> list[int]:
        return list(self.__phases)

    @phases.setter
    def phases(self, phases: list[int]):
        if self.__resetting or phases != self.__phases:
            self.__phases = phases
            self.reset()

    @property
    def current_phase(self) -> int:
        current_phase = 0
        if self.__current_phase_index < len(self.__phases):
            current_phase = self.__phases[self.__current_phase_index]
        return current_phase

    @property
    def next_phase(self) -> int:
        next_phase = 0
        if self.__current_phase_index + 1 < len(self.__phases):
            next_phase = self.__phases[self.__current_phase_index + 1]
        return next_phase

    @property
    def current_phase_index(self) -> int:
        return self.__current_phase_index

    @current_phase_index.setter
    def current_phase_index(self, index: int):
        if self.__resetting or index != self.__current_phase_index:
            self.__current_phase_index = index
            self.current_phase_changed.emit(self.current_phase)
            self.next_phase_changed.emit(self.next_phase)

    @property
    def current_phase_elapsed(self) -> int:
        return self.__current_phase_elapsed

    @current_phase_elapsed.setter
    def current_phase_elapsed(self, new_value: int):
        if self.__resetting or new_value != self.__current_phase_elapsed:
            self.__current_phase_elapsed = new_value
            self.current_phase_elapsed_changed.emit(new_value)

    def reset(self):
        self.__resetting = True
        self.current_phase_index = 0
        self.current_phase_elapsed = 0
        total_time = sum(self.__phases)
        self.minutes_before_target_changed.emit(total_time // 60_000)
        self.__resetting = False
