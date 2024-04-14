import importlib.resources

from PySide6.QtGui import QFontDatabase, QFont
from PySide6.QtWidgets import QWidget

from .enum_combo_box import EnumComboBox
from .scroll_widget import ScrollWidget
from .thread import DelegatingQThread


def set_class(self: QWidget, classes: list[str]) -> None:
    self.setProperty('class', ' '.join(classes))


def get_font(font_data: bytes, point_size: int = -1) -> QFont:
    application_font = QFontDatabase.addApplicationFontFromData(font_data)
    font_families = QFontDatabase.applicationFontFamilies(application_font)
    font_family = next(iter(font_families))
    return QFont(font_family, point_size)
