import importlib
import inspect
import pkgutil
from typing import Type, TypeVar

from .component import get_definitions, ComponentFactory, ComponentDefinitions, Scope
from .provider import Provider, SingletonProvider, PrototypeProvider

T = TypeVar('T')


class AppContext:
    def __init__(self, base_packages: list[str]):
        self.__providers: dict[type, Provider] = {}

        for package in base_packages:
            module = importlib.import_module(package)
            for _, module_name, _ in pkgutil.iter_modules(module.__path__):
                module_path = '.'.join([package, module_name])
                importlib.import_module(module_path)

        component_definitions = get_definitions()
        dependency_graph = self.__build_dependency_graph(component_definitions)
        for component_type, dependencies in dependency_graph.items():
            self.__get_or_create_provider(component_definitions, component_type)

    def get_component(self, component_type: Type[T]) -> T:
        if component_type not in self.__providers:
            raise ValueError(f'Component {component_type} not found')
        provider: Provider = self.__providers[component_type]
        return provider.get()

    @staticmethod
    def __build_dependency_graph(component_definitions: ComponentDefinitions):
        def get_signature(_factory: ComponentFactory):
            if isinstance(_factory, type):
                return inspect.signature(_factory.__init__)
            else:
                return inspect.signature(_factory)

        graph = {}
        for key, (scope, factory) in component_definitions.items():
            signature = get_signature(factory)
            dependencies = [
                param.annotation
                for name, param in signature.parameters.items()
                if name != 'self' and param.annotation is not inspect.Signature.empty
            ]
            graph[key] = dependencies
        return graph

    def __get_or_create_provider(self,
                                 component_definitions: ComponentDefinitions,
                                 component_type: type) -> Provider:
        if component_type in self.__providers:
            return self.__providers[component_type]

        scope, component_factory = component_definitions[component_type]
        if isinstance(component_factory, type):
            signature = inspect.signature(component_factory.__init__)
        else:
            signature = inspect.signature(component_factory)

        dependency_providers = [
            self.__get_or_create_provider(component_definitions, param.annotation)
            for name, param in signature.parameters.items()
            if name != 'self' and param.annotation is not inspect.Signature.empty
        ]

        provider = self.__get_provider(scope, component_factory, dependency_providers)
        self.__providers[component_type] = provider
        return provider

    @staticmethod
    def __get_provider(scope: Scope, factory: ComponentFactory, dependency_providers: list[Provider]) -> Provider:
        if scope == Scope.SINGLETON:
            return SingletonProvider(factory, dependency_providers)
        else:
            return PrototypeProvider(factory, dependency_providers)
