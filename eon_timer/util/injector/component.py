import enum
import inspect
from typing import Callable, Final


class Scope(enum.Enum):
    SINGLETON = enum.auto()
    PROTOTYPE = enum.auto()


ComponentFactory: Final = type | Callable
ComponentDefinition: Final = tuple[Scope, ComponentFactory]
ComponentDefinitions: Final = dict[type, ComponentDefinition]

__definitions: ComponentDefinitions = {}


def component(scope: Scope = Scope.SINGLETON):
    def decorate_class(cls):
        __definitions[cls] = (scope, cls)
        return cls

    def decorate_function(func):
        sig = inspect.signature(func)
        if sig.return_annotation is inspect.Signature.empty:
            raise ValueError('Component function must have a return type hint')
        __definitions[sig.return_annotation] = (scope, func)
        return func

    def decorate(decorated):
        if isinstance(decorated, type):
            return decorate_class(decorated)
        elif isinstance(decorated, Callable):
            return decorate_function(decorated)
        raise ValueError('Component decorator must be applied to a class or function')

    return decorate


def get_definitions() -> ComponentDefinitions:
    return dict(__definitions)
