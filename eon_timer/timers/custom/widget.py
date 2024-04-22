import functools
from typing import Final

from PySide6.QtGui import Qt
from PySide6.QtWidgets import QWidget, QGridLayout, QListWidget, QSpinBox, QPushButton, QHBoxLayout, QVBoxLayout, \
    QComboBox, QLineEdit

from eon_timer.util.const import INT_MAX
from eon_timer.util.injector import component
from .model import CustomTimerModel
from ...util import pyside


@component()
class CustomTimerWidget(QWidget):
    def __init__(self, model: CustomTimerModel):
        super().__init__()
        self.model: Final[CustomTimerModel] = model
        self.__spinbox: Final[QSpinBox] = QSpinBox()
        self.__init_components()

    def __init_components(self):
        layout = QVBoxLayout(self)
        layout.setSpacing(10)

        widget = QWidget()
        widget_layout = QHBoxLayout(widget)
        widget_layout.setSpacing(10)
        for index, phase in enumerate(self.model.phases.get()):
            widget_layout.addWidget(PhaseWidget(self.model, index))
        layout.addWidget(widget, stretch=1, alignment=Qt.AlignmentFlag.AlignTop)

        button = QPushButton('+')
        layout.addWidget(button, alignment=Qt.AlignmentFlag.AlignBottom)

    def create_phases(self) -> list[int]:
        return self.model.phases.get()

    def __on_add(self):
        value = self.__spinbox.value()
        self.model.phases.add_item(value)

    def __on_remove(self):
        print('remove')

    def __on_move_up(self):
        print('move up')

    def __on_move_down(self):
        print('move down')

    def __item_selection_changed(self, list_widget: QListWidget):
        print(list_widget.selectedItems())


class PhaseWidget(QWidget):
    def __init__(self,
                 model: CustomTimerModel,
                 index: int):
        super().__init__()
        self.__value_field: Final[QLineEdit] = QLineEdit()
        self.__unit_field: Final[QComboBox] = QComboBox()
        self.__init_components()

    def __init_components(self):
        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)
        layout.setSpacing(5)

        self.__unit_field.addItems(['ms', 'Frames', 'Hex'])
        layout.addWidget(self.__value_field, stretch=1)
        layout.addWidget(self.__unit_field, stretch=1)

        button_bar = QWidget()
        button_bar_layout = QHBoxLayout(button_bar)
        button_bar_layout.setContentsMargins(0, 0, 0, 0)
        button_bar_layout.setSpacing(0)
        pyside.set_class(button_bar, ['button-bar'])
        layout.addWidget(button_bar)

        button = QPushButton('⬆')
        pyside.set_class(button, ['first'])
        button_bar_layout.addWidget(button, stretch=0)

        button = QPushButton('⬇')
        button_bar_layout.addWidget(button, stretch=0)

        button = QPushButton('-')
        pyside.set_class(button, ['last'])
        button_bar_layout.addWidget(button, stretch=0)
