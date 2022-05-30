import typing

from PySide6.QtWidgets import *


def setClass(self: QWidget, classes: typing.List[str]):
    self.setProperty('class', ' '.join(classes))


def add_row(layout: QGridLayout, label: QLabel, field: QWidget):
    label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
    field.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
    layout.addWidget(label, )
