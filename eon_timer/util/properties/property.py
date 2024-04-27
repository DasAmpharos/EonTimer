from typing import TypeVar, Generic, Self, Type, Final

from .property_change import PropertyChangeEvent, PropertyChangeListener

T = TypeVar('T')


class Property(Generic[T]):
    def __init__(self,
                 initial_value: T | None = None,
                 value_type: Type[T] | None = None,
                 transient: bool = False):
        if value_type is None and initial_value is None:
            raise ValueError('value_type must be specified if initial_value is None')
        self.initial_value: Final[T | None] = initial_value
        self.value_type: Final[Type[T]] = value_type or type(initial_value)
        self.__change_listeners: list[PropertyChangeListener] = []
        self.__transient = transient
        self._value = initial_value

    def dispose(self):
        self.__change_listeners.clear()

    def on_change(self, listener: PropertyChangeListener):
        self.__change_listeners.append(listener)

    def update(self, other: Self):
        self.set(other.get())

    def get(self) -> T:
        return self._value

    def set(self, new_value: T) -> None:
        if new_value != self._value:
            old_value = self._value
            self._value = new_value
            event = PropertyChangeEvent(old_value, new_value)
            for listener in self.__change_listeners:
                listener(event)

    def reset(self):
        self.set(self.initial_value)

    @property
    def transient(self) -> bool:
        return self.__transient


class IntProperty(Property[int]):
    def __init__(self, initial_value: int | None = None, transient: bool = False):
        super().__init__(initial_value, int, transient)

    def add(self, value: int):
        self.set(self.get() + value)

    def sub(self, value: int):
        self.set(self.get() - value)

    def mul(self, value: int):
        self.set(self.get() * value)

    def div(self, value: int):
        self.set(int(self.get() / value))


class FloatProperty(Property[float]):
    def __init__(self, initial_value: float | None = None, transient: bool = False):
        Property.__init__(self, initial_value, float, transient)

    def add(self, value: float):
        self.set(self.get() + value)

    def sub(self, value: float):
        self.set(self.get() - value)

    def mul(self, value: float):
        self.set(self.get() * value)

    def div(self, value: float):
        self.set(self.get() / value)
