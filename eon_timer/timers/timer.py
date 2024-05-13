from abc import abstractmethod
from typing import Generic, TypeVar

ModelT = TypeVar('ModelT')


class Timer(Generic[ModelT]):
    @abstractmethod
    def create(self, model: ModelT) -> list[float]:
        ...

    @abstractmethod
    def calibrate(self, model: ModelT):
        ...
