from dataclasses import dataclass
from typing import Callable, Final, Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QLabel, QWidget

AddWidget = Callable[[QWidget, int, int], None]
RemoveWidget = Callable[[QWidget], None]


@dataclass
class FieldSet:
    row: Final[int]
    field: Final[QWidget]
    label: Final[Optional[QLabel]]

    def __init__(self,
                 row: int,
                 field: QWidget,
                 label: Optional[QLabel],
                 add_widget: AddWidget,
                 remove_widget: RemoveWidget,
                 is_visible: bool = True) -> None:
        self.row = row
        self.field = field
        self.label = label
        self.__add_widget = add_widget
        self.__remove_widget = remove_widget
        self.__visible = is_visible

    @property
    def visible(self) -> bool:
        return self.__visible

    @visible.setter
    def visible(self, value: bool) -> None:
        if self.__visible != value:
            (self.show if value else self.hide)()
            self.__visible = value

    def show(self) -> None:
        col = 0
        if self.label is not None:
            self.__add_widget(self.label, self.row, col, Qt.AlignRight)
            self.label.show()
            col += 1
        self.__add_widget(self.field, self.row, col)
        self.field.show()

    def hide(self) -> None:
        if self.label is not None:
            self.__remove_widget(self.label)
            self.label.hide()
        self.__remove_widget(self.field)
        self.field.hide()
