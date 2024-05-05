from PySide6.QtGui import QFontDatabase
from PySide6.QtWidgets import QWidget

from .enum_combo_box import EnumComboBox
from .thread import DelegatingQThread


def set_class(self: QWidget, classes: list[str]) -> None:
    self.setProperty('class', ' '.join(classes))


def append_class(self: QWidget, classes: list[str]) -> None:
    current = self.property('class')
    if current:
        classes = current.split(' ') + classes
    self.setProperty('class', ' '.join(classes))


def install_font(font_data: bytes) -> list[str]:
    application_font = QFontDatabase.addApplicationFontFromData(font_data)
    return QFontDatabase.applicationFontFamilies(application_font)
