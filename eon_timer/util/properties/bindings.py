import functools
from typing import Final, TypeVar

from PySide6.QtWidgets import QCheckBox, QComboBox, QDoubleSpinBox, QLineEdit, QSpinBox

from eon_timer.util import loggers
from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.pyside import EnumComboBox
from .property import Property, PropertyChangeEvent

T = TypeVar('T')
EnhancedEnumT = TypeVar('EnhancedEnumT', bound=EnhancedEnum)

logger: Final = loggers.get_logger(__name__)


def bind(left: Property[T],
         right: Property[T],
         bidirectional: bool = False):
    def on_change(other: Property[T], event: PropertyChangeEvent[T]):
        other.set(event.new_value)

    left.update(right)
    right.on_change(functools.partial(on_change, left))
    if bidirectional:
        left.on_change(functools.partial(on_change, right))


def bind_str_combobox(widget: QComboBox,
                      p_property: Property[str]) -> None:
    logger.debug('bind_str_combobox(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_property_changed(event: PropertyChangeEvent[str]) -> None:
        if event.new_value != widget.currentText():
            widget.setCurrentText(event.new_value)

    widget.setCurrentText(p_property.get())
    widget.currentTextChanged.connect(p_property.set)
    p_property.on_change(on_property_changed)


def bind_enum_combobox(widget: EnumComboBox[EnhancedEnumT],
                       p_property: Property[EnhancedEnumT]) -> None:
    logger.debug('bind_enum_combobox(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_value_changed(event: PropertyChangeEvent[EnhancedEnumT]) -> None:
        p_property.set(event.new_value)

    def on_property_changed(event: PropertyChangeEvent[EnhancedEnumT]) -> None:
        widget.set_value(event.new_value)

    widget.value.update(p_property)
    widget.value.on_change(on_value_changed)
    p_property.on_change(on_property_changed)


def bind_checkbox(widget: QCheckBox,
                  p_property: Property[bool]) -> None:
    logger.debug('bind_checkbox(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_property_changed(event: PropertyChangeEvent[bool]) -> None:
        if event.new_value != widget.isChecked():
            widget.setChecked(event.new_value)

    widget.setChecked(p_property.get())
    widget.stateChanged.connect(p_property.set)
    p_property.on_change(on_property_changed)


def bind_line_edit(widget: QLineEdit,
                   p_property: Property[str]) -> None:
    logger.debug('bind_line_edit(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_property_changed(event: PropertyChangeEvent[str]) -> None:
        if event.new_value != widget.text():
            widget.setText(event.new_value)

    widget.setText(p_property.get())
    widget.textChanged.connect(p_property.set)
    p_property.on_change(on_property_changed)


def bind_spinbox(widget: QSpinBox,
                 p_property: Property[int]) -> None:
    logger.debug('bind_spinbox(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_property_changed(event: PropertyChangeEvent[int]) -> None:
        if event.new_value != widget.value():
            widget.setValue(event.new_value)

    widget.setValue(p_property.get())
    widget.valueChanged.connect(p_property.set)
    p_property.on_change(on_property_changed)


def bind_float_spinbox(widget: QDoubleSpinBox,
                       p_property: Property[float]) -> None:
    logger.debug('bind_float_spinbox(widget=%s, property=%s)', widget.objectName(), p_property.get())

    def on_property_changed(event: PropertyChangeEvent[float]) -> None:
        if event.new_value != widget.value():
            widget.setValue(event.new_value)

    widget.setValue(p_property.get())
    widget.valueChanged.connect(p_property.set)
    p_property.on_change(on_property_changed)
