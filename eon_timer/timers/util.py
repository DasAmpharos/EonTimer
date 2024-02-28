from typing import Final

MINIMUM_LENGTH: Final[int] = 14000


def to_minimum_length(value: int) -> int:
    while value < MINIMUM_LENGTH:
        value += 60000
    return value
