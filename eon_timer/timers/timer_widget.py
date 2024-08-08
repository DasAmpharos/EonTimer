from abc import abstractmethod
from typing import Final, Generic, TypeVar, final

from PySide6.QtCore import Signal

from eon_timer.util.properties.settings import Settings
from .timer import Timer

ModelT = TypeVar('ModelT', bound=Settings)
TimerT = TypeVar('TimerT', bound=Timer)


class TimerWidget(Generic[ModelT, TimerT]):
    timer_changed: Final = Signal()

    def __init__(self,
                 model: ModelT,
                 timer: TimerT):
        self.model: Final = model
        self.timer: Final = timer
        self.__resetting = False
        self._init_components()
        self._init_listeners()

    @abstractmethod
    def _init_components(self):
        ...

    def _init_listeners(self):
        pass

    def create_phases(self) -> list[float]:
        return self.timer.create(self.model)

    def calibrate(self):
        self.timer.calibrate(self.model)

    @final
    def reset(self):
        self.__resetting = True
        self.model.reset()
        self._reset()
        self.timer_changed.emit()
        self.__resetting = False

    def _reset(self):
        pass

    @property
    def resetting(self) -> bool:
        return self.__resetting
