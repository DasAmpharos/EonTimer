from enum import Enum
from typing import Callable, Final, Generic, Self, Type, TypeVar, final, override

from PySide6.QtCore import QSettings

from eon_timer.util import loggers

from .property_change import PropertyChangeEvent, PropertyChangeListener

T = TypeVar('T')


class Property(Generic[T]):
    def __init__(self, value: T | None = None, transient: bool = False):
        self.logger: Final = loggers.get_logger(self)

        self._value: T = value
        self._initial_value: Final = value
        self.__change_listeners: list[PropertyChangeListener] = []
        self.__transient: Final = transient

    @final
    def read(self, settings: QSettings, name: str):
        self._value = self._read(settings, name)
        self.logger.debug(f'read(name={name}) -> {self._value}')

    def _read(self, settings: QSettings, name: str):
        return settings.value(name, self._initial_value)

    @final
    def write(self, settings: QSettings, name: str):
        self._write(settings, name)
        self.logger.debug(f'write(name={name}, value={self._value})')

    def _write(self, settings: QSettings, name: str):
        settings.setValue(name, self._value)

    def on_change(self, listener: PropertyChangeListener):
        self.__change_listeners.append(listener)

    def dispose(self):
        self.__change_listeners.clear()

    def update(self, other: Self):
        self.set(other.get())

    def get(self) -> T:
        return self._value

    def set(self, new_value: T) -> None:
        if new_value != self._value:
            old_value = self._value
            self._value = new_value
            event = PropertyChangeEvent(old_value, new_value)
            self.notify(event)

    def reset(self):
        self.set(self._initial_value)

    def fresh(self) -> 'Property[T]':
        """Create a new instance with the same type/initial-value/transient, but no listeners."""
        return type(self)(self._initial_value, self.transient)

    @final
    def notify(self, event: PropertyChangeEvent[T]):
        for listener in list(self.__change_listeners):
            listener(event)

    @property
    def transient(self) -> bool:
        return self.__transient


class BoolProperty(Property[bool]):
    def _read(self, settings: QSettings, name: str):
        return settings.value(name, self._initial_value, bool)


class IntProperty(Property[int]):
    @override
    def _read(self, settings: QSettings, name: str):
        return settings.value(name, self._initial_value, int)

    def add(self, value: int):
        self.set(self._value + value)

    def sub(self, value: int):
        self.set(self._value - value)

    def mul(self, value: int):
        self.set(self._value * value)

    def div(self, value: int):
        self.set(int(self._value / value))


class FloatProperty(Property[float]):
    @override
    def _read(self, settings: QSettings, name: str):
        return settings.value(name, self._initial_value, float)

    def add(self, value: float):
        self.set(self._value + value)

    def sub(self, value: float):
        self.set(self._value - value)

    def mul(self, value: float):
        self.set(self._value * value)

    def div(self, value: float):
        self.set(self._value / value)


EnumT = TypeVar('EnumT', bound=Enum)


class EnumProperty(Property[EnumT]):
    def __init__(self, value: EnumT | None = None, enum_type: Type[EnumT] | None = None, transient: bool = False):
        Property.__init__(self, value, transient)
        if enum_type is None and value is None:
            raise ValueError('enum_type must be specified if initial_value is None')
        self.enum_type: Final = enum_type or type(value)

    @override
    def fresh(self) -> 'EnumProperty[EnumT]':
        return EnumProperty(self._initial_value, self.enum_type, self.transient)

    @override
    def _read(self, settings: QSettings, name: str):
        initial_value = None
        if self._initial_value is not None:
            initial_value = str(self._initial_value)
        value = settings.value(name, initial_value)
        try:
            return self.enum_type(value)
        except (ValueError, KeyError):
            return self._initial_value


class ListProperty(Property[list[T]]):
    ElementReader: Final = Callable[[QSettings], T]
    ElementWriter: Final = Callable[[QSettings, T], None]

    def __init__(
        self,
        value: list[T] | None = None,
        element_type: Type[T] | None = None,
        element_reader: ElementReader | None = None,
        element_writer: ElementWriter | None = None,
        transient: bool = False,
    ):
        Property.__init__(self, value, transient)
        if element_type is None and (value is None or len(value) == 0):
            raise ValueError('element_type must be specified if initial_value is None or empty')
        self.element_type: Final = element_type or type(value[0])
        self.element_reader: Final = element_reader or self.__default_element_reader
        self.element_writer: Final = element_writer or self.__default_element_writer

    @override
    def _read(self, settings: QSettings, name: str):
        value_array = []
        count = settings.beginReadArray(name)
        for i in range(count):
            settings.setArrayIndex(i)
            self.logger.debug(f'setArrayIndex({i})')
            value = self.element_reader(settings)
            value_array.append(value)
        settings.endArray()
        return value_array

    @override
    def _write(self, settings: QSettings, name: str):
        count = len(self._value)
        settings.beginWriteArray(name, count)
        for i, value in enumerate(self._value):
            settings.setArrayIndex(i)
            self.logger.debug(f'setArrayIndex({i})')
            self.element_writer(settings, value)
        settings.endArray()

    def append(self, value: T):
        self._value.append(value)
        event = PropertyChangeEvent(self._value, self._value)
        self.notify(event)

    def remove(self, value: T):
        self._value.remove(value)
        event = PropertyChangeEvent(self._value, self._value)
        self.notify(event)

    def clear(self):
        self._value.clear()
        event = PropertyChangeEvent(self._value, self._value)
        self.notify(event)

    def __iter__(self):
        return iter(self._value)

    def __len__(self):
        return len(self._value)

    @override
    def fresh(self) -> 'ListProperty[T]':
        initial = list(self._initial_value) if self._initial_value else []
        return ListProperty(initial, self.element_type, self.element_reader, self.element_writer, self.transient)

    @staticmethod
    def __default_element_reader(settings: QSettings) -> T:
        return settings.value('value')

    @staticmethod
    def __default_element_writer(settings: QSettings, value: T):
        settings.setValue('value', value)
