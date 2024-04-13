import atexit
from abc import abstractmethod
from typing import Final

from .component import ComponentFactory
from .lifecycle import CloseListener


class Provider:
    @abstractmethod
    def get(self) -> any:
        ...


class SingletonProvider(Provider):
    def __init__(self,
                 factory: ComponentFactory,
                 dependency_providers: list[Provider]):
        dependencies = list(map(lambda it: it.get(), dependency_providers))
        self.instance: Final = factory(*dependencies)
        if isinstance(self.instance, CloseListener):
            atexit.register(getattr(self.instance, '_on_close'))

    def get(self) -> any:
        return self.instance


class PrototypeProvider(Provider):
    def __init__(self,
                 dependency_providers: list[Provider],
                 factory: ComponentFactory):
        self.__dependency_providers = dependency_providers
        self.__factory = factory

    def get(self) -> any:
        dependencies = list(map(lambda it: it.get(), self.__dependency_providers))
        instance = self.__factory(*dependencies)
        if isinstance(instance, CloseListener):
            atexit.register(getattr(instance, '__on_close'))
        return instance
