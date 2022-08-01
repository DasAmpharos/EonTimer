from typing import Final, Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGridLayout, QLabel, QSizePolicy, QWidget


class FormLayout:
    """ Wrapper around QGridLayout """

    def __init__(self, parent: QWidget, spacing: int = 10) -> None:
        self.__layout: Final[QGridLayout] = QGridLayout(parent)
        self.__layout.setSpacing(spacing)
        self.__current_row = 0

    def add_row(self,
                field: QWidget,
                label: Optional[QLabel] = None) -> int:
        row = self.__current_row
        self.set_row(row, field, label)
        self.__current_row += 1
        return row

    def set_row(self,
                row: int,
                field: QWidget,
                label: Optional[QLabel] = None) -> None:
        column = 0 if label is None else 1
        column_span = 2 if label is None else 1

        if label is not None:
            label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
            self.__layout.addWidget(label, row, 0, Qt.AlignRight)

        field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        self.__layout.addWidget(field, row, column, 1, column_span)

    def remove_row(self,
                   row: int) -> None:
        i1 = self.__layout.itemAtPosition(row, 0)
        i2 = self.__layout.itemAtPosition(row, 1)

        w1 = i1.widget()
        w2 = i2.widget()
        self.__layout.removeWidget(w1)
        if w2 is not w1:
            self.__layout.removeWidget(w2)

    def set_content_margins(self, left: int, top: int, right: int, bottom: int) -> None:
        self.__layout.setContentsMargins(left, top, right, bottom)

    def set_alignment(self, alignment: Qt.AlignmentFlag) -> None:
        self.__layout.setAlignment(alignment)
