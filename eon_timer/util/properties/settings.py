from abc import abstractmethod
from typing import override, Final

from PySide6.QtCore import QSettings, Signal, QObject

from eon_timer.util.injector.lifecycle import CloseListener
from .property import Property


class Settings(QObject, CloseListener):
    settings_changed: Final[Signal] = Signal()

    def __init__(self, settings: QSettings):
        QObject.__init__(self, None)

        self.settings: Final[QSettings] = settings
        self.__properties: Final[dict[str, Property]] = {}

        self_type = type(self)
        for name, value in self_type.__dict__.items():
            if isinstance(value, Property):
                self.__properties[name] = value
                setattr(self, name, value)
        self._deserialize()

    def _deserialize(self):
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if self.settings.contains(name):
                from_settings = self.settings.value(name, None, prop.value_type)
                prop.set(from_settings)
        self.settings.endGroup()

    def _serialize(self):
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if not prop.transient:
                self.settings.setValue(name, prop.get())
        self.settings.endGroup()

    @property
    def properties(self) -> dict[str, Property]:
        return dict(self.__properties)

    @override
    def _on_close(self):
        self._serialize()

    def reset(self):
        for prop in self.__properties.values():
            prop.reset()
        self.settings_changed.emit()

    @property
    @abstractmethod
    def group(self) -> str:
        ...
