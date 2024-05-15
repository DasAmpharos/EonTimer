from abc import abstractmethod
from typing import Final, override

from PySide6.QtCore import QObject, QSettings, Signal

from eon_timer.util import loggers
from eon_timer.util.injector.lifecycle import CloseListener
from .property import Property


class Settings(QObject, CloseListener):
    settings_changed: Final = Signal()

    def __init__(self, settings: QSettings):
        QObject.__init__(self, None)

        self.logger: Final = loggers.get_logger(self)

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
        self.logger.debug('beginGroup(%s)', self.group)
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if not prop.transient:
                prop.read(self.settings, name)
        self.settings.endGroup()
        self.logger.debug('endGroup(%s)', self.group)

    def _write(self):
        self.logger.debug('Writing settings: %s', self.group)
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
