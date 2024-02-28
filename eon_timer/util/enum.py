from enum import Enum
from typing import Type, Self


class EnhancedEnum(Enum):
    __cached_values = None

    @classmethod
    def get(cls, index: int) -> Self:
        values = cls.values()
        return values[index]

    @classmethod
    def index_of(cls, e: Self) -> int:
        values = cls.values()
        return values.index(e)

    @classmethod
    def values(cls: Type[Self]) -> tuple[Self, ...]:
        if cls.__cached_values is None:
            cls.__cached_values = tuple(cls)
        return cls.__cached_values
