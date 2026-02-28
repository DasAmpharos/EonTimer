from abc import abstractmethod
from typing import Final

from PySide6.QtCore import QObject, QSettings, Signal

from eon_timer.util import loggers

from .property import Property, PropertyChangeEvent


class Settings(QObject):
    settings_changed: Final = Signal()

    def __init__(self, settings: QSettings):
        QObject.__init__(self, None)

        self.logger: Final = loggers.get_logger(self)

        self.settings: Final = settings
        self.__properties: Final[dict[str, Property]] = {}

        self_type = type(self)
        for name, value in self_type.__dict__.items():
            if isinstance(value, Property):
                instance_prop = value.fresh()
                self.__properties[name] = instance_prop
                setattr(self, name, instance_prop)
        self._read()
        # Write-through: persist to QSettings immediately on any property change.
        # This means timer field edits auto-save as the user types, and settings
        # dialog changes persist the moment OK is pressed (model is updated).
        for prop in self.__properties.values():
            if not prop.transient:
                prop.on_change(self.__on_property_changed)

    def __on_property_changed(self, _: PropertyChangeEvent) -> None:
        self._write()

    def _read(self):
        self.logger.debug(f'beginGroup({self.group})')
        self.settings.beginGroup(self.group)
        for name, prop in self.__properties.items():
            if not prop.transient:
                prop.read(self.settings, name)
        self.settings.endGroup()
        self.logger.debug(f'endGroup({self.group})')

    def _write(self):
        self.logger.debug(f'Writing settings: {self.group}')
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
    def group(self) -> str: ...
