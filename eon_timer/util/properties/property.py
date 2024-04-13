from typing import TypeVar, Generic, Self

from .property_change import PropertyChangeEvent, PropertyChangeListener

T = TypeVar('T')


class Property(Generic[T]):
    def __init__(self, initial_value: T | None = None, transient: bool = False):
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
