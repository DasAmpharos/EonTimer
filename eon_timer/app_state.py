from typing import Final

from PySide6.QtCore import QObject, Signal

from eon_timer.util.injector import component


@component()
class AppState(QObject):
    phases_changed: Final[Signal] = Signal(list)
    current_phase_changed: Final[Signal] = Signal(float)
    current_phase_elapsed_changed: Final[Signal] = Signal(float)
    minutes_before_target_changed: Final[Signal] = Signal(int)
    next_phase_changed: Final[Signal] = Signal(float)

    running_changed: Final[Signal] = Signal(bool)
    action_triggered: Final[Signal] = Signal()

    def __init__(self):
        super().__init__()
        self.__phases: list[float] = []
        self.__current_phase_index: int = 0
        self.__current_phase_elapsed: float = 0
        self.__resetting = False
        self.__running = False

    @property
    def phases(self) -> list[float]:
        return list(self.__phases)

    @phases.setter
    def phases(self, phases: list[float]):
        if self.__resetting or phases != self.__phases:
            self.__phases = list(phases)
            self.reset()

    @property
    def current_phase(self) -> float:
        current_phase = 0
        if self.__current_phase_index < len(self.__phases):
            current_phase = self.__phases[self.__current_phase_index]
        return current_phase

    @property
    def next_phase(self) -> float:
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
    def current_phase_elapsed(self) -> float:
        return self.__current_phase_elapsed

    @current_phase_elapsed.setter
    def current_phase_elapsed(self, new_value: float):
        if self.__resetting or new_value != self.__current_phase_elapsed:
            self.__current_phase_elapsed = new_value
            self.current_phase_elapsed_changed.emit(new_value)

    @property
    def running(self) -> bool:
        return self.__running

    @running.setter
    def running(self, new_value: bool):
        if new_value != self.__running:
            self.__running = new_value
            self.running_changed.emit(new_value)

    def trigger_action(self):
        self.action_triggered.emit()

    def reset(self):
        self.__resetting = True
        self.current_phase_index = 0
        self.current_phase_elapsed = 0.0
        total_time = sum(self.__phases)
        self.minutes_before_target_changed.emit(int(total_time // 60_000))
        self.__resetting = False
