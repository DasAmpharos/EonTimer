from dataclasses import dataclass
from typing import Generic, Callable, TypeVar

T = TypeVar('T')


@dataclass
class PropertyChangeEvent(Generic[T]):
    old_value: T | None
    new_value: T | None


PropertyChangeListener = Callable[[PropertyChangeEvent[T]], None]


@dataclass
class ListPropertyChangeEvent(Generic[T]):
    pass
