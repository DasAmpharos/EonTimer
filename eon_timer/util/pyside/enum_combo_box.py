from typing import Final, Generic, Type, TypeVar

from PySide6.QtCore import QObject
from PySide6.QtWidgets import QComboBox

from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.properties.property import EnumProperty, PropertyChangeEvent

EnhancedEnumT = TypeVar('EnhancedEnumT', bound=EnhancedEnum)


class EnumComboBox(QComboBox, Generic[EnhancedEnumT]):
    def __init__(self,
                 enum_type: Type[EnhancedEnumT],
                 initial_value: EnhancedEnumT | None = None,
                 parent: QObject | None = None):
        QComboBox.__init__(self, parent)
        self.enum_type: Final = enum_type
        self.value: Final = EnumProperty(enum_type=enum_type)
        self.currentIndexChanged.connect(self.__on_current_index_changed)
        self.value.on_change(self.__on_property_changed)
        for value in enum_type.values():
            self.addItem(str(value), value)
        if initial_value is not None:
            self.setCurrentText(str(initial_value))

    def get_value(self) -> EnhancedEnumT:
        return self.value.get()

    def set_value(self, new_value: EnhancedEnumT) -> None:
        self.value.set(new_value)

    def __on_current_index_changed(self, index: int):
        self.value.set(self.enum_type.get(index))

    def __on_property_changed(self, event: PropertyChangeEvent[EnhancedEnumT]):
        self.setCurrentText(str(event.new_value))
