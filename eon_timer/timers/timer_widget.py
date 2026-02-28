import functools
from abc import abstractmethod
from typing import Final, Generic, TypeVar, final

from PySide6.QtCore import Signal

from eon_timer.util import loggers
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.properties.settings import Settings

from .timer import Timer

ModelT = TypeVar('ModelT', bound=Settings)
TimerT = TypeVar('TimerT', bound=Timer)


class TimerWidget(Generic[ModelT, TimerT]):
    timer_changed: Final = Signal()

    def __init__(self, model: ModelT, timer: TimerT):
        self.model: Final = model
        self.timer: Final = timer
        self.__resetting = False
        self.__logger: Final = loggers.get_logger(self)
        self._init_components()
        self._init_listeners()

    @abstractmethod
    def _init_components(self): ...

    def _init_listeners(self):
        pass

    def _register_field_listeners(self, pairs: list[tuple[Property, str]]) -> None:
        """Register timer_changed listeners for a list of (property, field_name) pairs."""

        def on_field_changed(field_name: str, event: PropertyChangeEvent) -> None:
            if not self.resetting:
                self.__logger.info(f'{field_name}: {event.new_value}')
                self.timer_changed.emit()

        for prop, field_name in pairs:
            prop.on_change(functools.partial(on_field_changed, field_name))

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
