from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGridLayout, QLabel, QSizePolicy, QWidget
from .field_set import FieldSet


class FormLayout:
    """ Wrapper around QGridLayout """

    def __init__(self, parent: QWidget, spacing: int = 10) -> None:
        self.__layout = QGridLayout(parent)
        self.__layout.setSpacing(spacing)
        self.__current_row = 0

    def add_row(self,
                field_widget: QWidget,
                label: Optional[str] = None) -> FieldSet:
        row = self.__current_row
        column = 0 if label is None else 1
        columnSpan = 2 if label is None else 1
        layout = self.__layout

        label_widget = None
        if label is not None:
            label_widget = QLabel(label)
            label_widget.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
            layout.addWidget(label_widget, row, 0, Qt.AlignRight)

        field_widget.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        layout.addWidget(field_widget, row, column, 1, columnSpan)
        field_set = FieldSet(row,
                             field_widget,
                             label_widget,
                             layout.addWidget,
                             layout.removeWidget)
        self.__current_row += 1
        return field_set

    def set_content_margins(self, left: int, top: int, right: int, bottom: int) -> None:
        self.__layout.setContentsMargins(left, top, right, bottom)

    def set_alignment(self, alignment: Qt.AlignmentFlag) -> None:
        self.__layout.setAlignment(alignment)
