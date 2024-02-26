import os
from enum import StrEnum
from typing import TypeVar

from PySide6.QtWidgets import QWidget, QComboBox


def get_module_name(file: str) -> str:
    return os.path.basename(os.path.dirname(file))


def set_class(self: QWidget, classes: list[str]) -> None:
    self.setProperty('class', ' '.join(classes))


StrEnumT = TypeVar('StrEnumT', bound=StrEnum)


def add_items(field: QComboBox,
              item_type: type[StrEnumT],
              current_value: StrEnumT):
    for item in list(item_type):
        field.addItem(str(item), item)
    field.setCurrentText(str(current_value))
