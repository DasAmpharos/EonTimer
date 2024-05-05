from typing import Final

from PySide6.QtCore import QObject, Signal

from eon_timer.util import const
from eon_timer.util.injector import component


@component()
class AppState(QObject):
    current_phase_changed: Final = Signal(float)
    current_phase_elapsed_changed: Final = Signal(float)
    minutes_before_target_changed: Final = Signal(int)
    next_phase_changed: Final = Signal(float)

    running_changed: Final = Signal(bool)
    action_triggered: Final = Signal()

    def __init__(self):
        super().__init__()
        self.__phases: list[float] = []
        self.__current_phase_index: int = 0
        self.__current_phase_elapsed: float = 0
        self.__running = False

    @property
    def phases(self) -> list[float]:
        return self.__phases

    @phases.setter
    def phases(self, phases: list[float]):
        self.__phases = list(phases)
        self.reset()

    def set_phase(self, index: int, phase: float):
        self.__phases[index] = phase
        # update current_phase / next_phase
        match index:
            case self.current_phase_index:
                self.current_phase_changed.emit(phase)
            case self.next_phase_index:
                self.next_phase_changed.emit(phase)
        # update minutes_before_target
        remaining_phases = self.__phases[self.__current_phase_index:]
        remaining_phases[0] -= self.__current_phase_elapsed
        self.__update_minutes_before_target(remaining_phases)

    @property
    def current_phase(self) -> float:
        current_phase = 0
        if self.__current_phase_index < len(self.__phases):
            current_phase = self.__phases[self.__current_phase_index]
        return current_phase

    @property
    def current_phase_index(self) -> int:
        return self.__current_phase_index

    @current_phase_index.setter
    def current_phase_index(self, index: int):
        self.__current_phase_index = index
        self.current_phase_changed.emit(self.current_phase)
        self.next_phase_changed.emit(self.next_phase)

    @property
    def current_phase_elapsed(self) -> float:
        return self.__current_phase_elapsed

    @current_phase_elapsed.setter
    def current_phase_elapsed(self, new_value: float):
        self.__current_phase_elapsed = new_value
        self.current_phase_elapsed_changed.emit(new_value)

    @property
    def next_phase(self) -> float:
        next_phase = 0
        if self.__current_phase_index + 1 < len(self.__phases):
            next_phase = self.__phases[self.__current_phase_index + 1]
        return next_phase

    @property
    def next_phase_index(self) -> int:
        return self.__current_phase_index + 1

    @property
    def running(self) -> bool:
        return self.__running

    @running.setter
    def running(self, new_value: bool):
        if new_value != self.__running:
            self.__running = new_value
            self.running_changed.emit(new_value)
            self.reset()

    def reset(self):
        self.current_phase_index = 0
        self.current_phase_elapsed = 0.0
        self.__update_minutes_before_target(self.__phases)

    def __update_minutes_before_target(self, phases: list[float]):
        minutes_before_target = -1
        if const.INFINITY not in phases:
            minutes_before_target = int(sum(phases) // 60_000)
        self.minutes_before_target_changed.emit(minutes_before_target)
