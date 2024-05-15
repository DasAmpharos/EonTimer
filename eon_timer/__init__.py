from PySide6.QtCore import QSettings

from eon_timer.util.injector.component import component


@component()
def __settings() -> QSettings:
    return QSettings()
