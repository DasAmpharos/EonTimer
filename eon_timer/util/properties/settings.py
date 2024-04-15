import typing
from abc import abstractmethod
from typing import override, Final

from PySide6.QtCore import QSettings, QObject

from .property import Property
from ..injector.lifecycle import CloseListener


class Settings(CloseListener):
    def __init__(self, settings: QSettings):
        self_type = type(self)
        self.settings: Final[QSettings] = settings
        settings.beginGroup(self.group)
        for name, value in self_type.__dict__.items():
            if isinstance(value, Property):
                if settings.contains(name):
                    new_value = settings.value(name, None, value.value_type)
                    value.set(new_value)
                setattr(self, name, value)
        settings.endGroup()

    @override
    def _on_close(self):
        self_type = type(self)
        self.settings.beginGroup(self.group)
        for name, value in self_type.__dict__.items():
            if isinstance(value, Property) and not value.transient:
                self.settings.setValue(name, value.get())
        self.settings.endGroup()

    @property
    @abstractmethod
    def group(self) -> str:
        ...


def settings_class(group: str):
    def decorator(cls):
        properties = dict(filter(lambda kv: isinstance(kv[1], Property), cls.__dict__.items()))

        def __init__(self, settings: QSettings, *args, **kwargs):
            cls.__init__(self, *args, **kwargs)
            self.__settings = settings
            settings.beginGroup(group)
            for name, _property in properties.items():
                new_value = settings.value(name, None)
                if new_value is not None:
                    _property.value = new_value
                setattr(self, name, _property)
            settings.endGroup()

        def __on_close(self):
            self.__settings.beginGroup(group)
            for name, _property in properties.items():
                if not _property.transient:
                    self.__settings.setValue(name, _property.value)
            self.__settings.endGroup()

        def update(self, other):
            for name in properties.keys():
                self_property = getattr(self, name)
                other_property = getattr(other, name)
                self_property.update(other_property)

        new_class = type(cls.__name__, cls.__bases__, dict(cls.__dict__))
        setattr(new_class, '__init__', __init__)
        setattr(new_class, '__on_close', __on_close)
        setattr(new_class, 'update', update)
        return new_class

    return decorator
