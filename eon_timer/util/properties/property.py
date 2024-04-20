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
        self.value_type: Final[Type[T]] = value_type or type(initial_value)
        self.__change_listeners: list[PropertyChangeListener] = []
        self.__transient = transient
        self.__value = initial_value

    def on_change(self, listener: PropertyChangeListener):
        self.__change_listeners.append(listener)

    def update(self, other: Self):
        self.set(other.get())

    def get(self) -> T:
        return self.__value

    def set(self, new_value: T) -> None:
        if new_value != self.__value:
            old_value = self.__value
            self.__value = new_value
            event = PropertyChangeEvent(old_value, new_value)
            for listener in self.__change_listeners:
                listener(event)

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
