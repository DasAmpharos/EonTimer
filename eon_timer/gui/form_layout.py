from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGridLayout, QLabel, QSizePolicy, QWidget


class FormLayout:
    """ Wrapper around QGridLayout """

    def __init__(self, parent: QWidget, spacing: int = 10) -> None:
        self.__layout = QGridLayout(parent)
        self.__layout.setSpacing(spacing)
        self.__current_row = 0

    def add_row(self,
                field: QWidget,
                label: Optional[str] = None) -> None:
        row = self.__current_row
        column = 0 if label is None else 1
        columnSpan = 2 if label is None else 1

        if label is not None:
            _label = QLabel(label)
            _label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
            self.__layout.addWidget(_label, row, 0, Qt.AlignRight)

        field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        self.__layout.addWidget(field, row, column, 1, columnSpan)
        self.__current_row += 1
    
    def set_content_margins(self, left: int, top: int, right: int, bottom: int) -> None:
        self.__layout.setContentsMargins(left, top, right, bottom)
    
    def set_alignment(self, alignment: Qt.AlignmentFlag) -> None:
        self.__layout.setAlignment(alignment)
