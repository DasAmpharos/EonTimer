from abc import abstractmethod
from typing import Final, override

from PySide6.QtCore import QObject, QSettings, Signal

from eon_timer.util.injector.lifecycle import CloseListener
from .property import Property


class Settings(QObject, CloseListener):
    settings_changed: Final = Signal()

    def __init__(self, settings: QSettings):
        QObject.__init__(self, None)

        self.settings: Final = settings
        self.__properties: Final[dict[str, Property]] = {}

        self_type = type(self)
        for name, value in self_type.__dict__.items():
            if isinstance(value, Property):
                self.__properties[name] = value
                setattr(self, name, value)
        self._read()

    @override
    def _on_close(self):
        self._write()

    def _read(self):
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if not prop.transient:
                prop.read(self.settings, name)
        self.settings.endGroup()

    def _write(self):
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if not prop.transient:
                prop.write(self.settings, name)
        self.settings.endGroup()

    @property
    def properties(self) -> dict[str, Property]:
        return dict(self.__properties)

    def reset(self):
        for prop in self.__properties.values():
            prop.reset()
        self.settings_changed.emit()

    @property
    @abstractmethod
    def group(self) -> str:
        ...
