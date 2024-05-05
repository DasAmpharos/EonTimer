from typing import Final, Optional

from PySide6.QtWidgets import QLabel, QWidget

from .form_layout import FormLayout


class FieldSet:
    def __init__(self,
                 row: int,
                 field: QWidget,
                 label: Optional[QLabel],
                 layout: FormLayout,
                 enabled: bool = True,
                 visible: bool = True) -> None:
        self.row: Final = row
        self.field: Final = field
        self.label: Final = label
        self.layout: Final = layout
        self.__enabled = enabled
        self.__visible = visible

    @property
    def enabled(self) -> bool:
        return self.__enabled

    @enabled.setter
    def enabled(self, value: bool) -> None:
        self.set_enabled(value)

    def set_enabled(self, value: bool) -> None:
        if self.__enabled != value:
            (self.enable if value else self.disable)()

    @property
    def visible(self) -> bool:
        return self.__visible

    @visible.setter
    def visible(self, value: bool) -> None:
        if self.__visible != value:
            (self.show if value else self.hide)()

    def enable(self) -> None:
        self.field.setEnabled(True)
        if self.label is not None:
            self.label.setEnabled(True)
        self.__enabled = True

    def disable(self) -> None:
        self.field.setEnabled(False)
        if self.label is not None:
            self.label.setEnabled(False)
        self.__enabled = False

    def show(self) -> None:
        self.layout.set_row(self.row, self.field, self.label)
        if self.label is not None:
            self.label.show()
        self.field.show()
        self.__visible = True

    def hide(self) -> None:
        self.layout.remove_row(self.row)
        if self.label is not None:
            self.label.hide()
        self.field.hide()
        self.__visible = False
