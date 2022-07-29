from PySide6.QtWidgets import *


def set_class(self: QWidget, classes: list[str]) -> None:
    self.setProperty('class', ' '.join(classes))


def add_row(self: QGridLayout, label: QLabel, field: QWidget) -> None:
    label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
    field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
    # layout.addWidget(label, )
