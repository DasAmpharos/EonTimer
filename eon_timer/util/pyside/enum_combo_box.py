from typing import Final, Generic, Type, TypeVar

from PySide6.QtCore import QObject, Signal
from PySide6.QtWidgets import QComboBox

from eon_timer.util.enum import EnhancedEnum

EnhancedEnumT = TypeVar('EnhancedEnumT', bound=EnhancedEnum)


class EnumComboBox(QComboBox, Generic[EnhancedEnumT]):
    value_changed: Final = Signal(object)

    def __init__(
        self, enum_type: Type[EnhancedEnumT], initial_value: EnhancedEnumT | None = None, parent: QObject | None = None
    ):
        QComboBox.__init__(self, parent)
        self.enum_type: Final = enum_type
        self.currentIndexChanged.connect(self.__on_current_index_changed)
        for value in enum_type.values():
            self.addItem(str(value), value)
        if initial_value is not None:
            self.set_value(initial_value)

    def get_value(self) -> EnhancedEnumT:
        return self.enum_type.get(self.currentIndex())

    def set_value(self, new_value: EnhancedEnumT) -> None:
        self.setCurrentText(str(new_value))

    def __on_current_index_changed(self, index: int):
        self.value_changed.emit(self.enum_type.get(index))
