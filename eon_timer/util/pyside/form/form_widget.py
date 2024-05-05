from enum import StrEnum
from typing import Final, Optional

from PySide6.QtWidgets import QLabel, QWidget

from eon_timer.util.pyside.name_service import NameService
from .field_set import FieldSet
from .form_layout import FormLayout


class FormWidget(QWidget):
    class Field(StrEnum):
        pass

    def __init__(self,
                 name_service: NameService,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        typename = type(self)
        self._layout: Final = FormLayout(self)
        self.__field_sets: Final[dict[typename.Field, FieldSet]] = {}
        self.name_service: Final = name_service

    def add_field(self,
                  field: Field,
                  widget: QWidget,
                  with_label: bool = True,
                  layout: Optional[FormLayout] = None,
                  visible: bool = True,
                  name: str | None = None) -> FieldSet:
        layout = layout or self._layout
        label = QLabel(str(field)) if with_label else None
        if name is not None:
            if label is not None:
                self.name_service.set_name(label, f'{name}Label')
            self.name_service.set_name(widget, f'{name}Field')

        row = layout.add_row(widget, label)
        field_set = FieldSet(row, widget, label, layout)
        self.__field_sets[field] = field_set
        field_set.visible = visible
        return field_set

    def set_visible(self, field: Field, visible: bool) -> None:
        field_set = self.__field_sets.get(field, None)
        if field_set is not None:
            field_set.visible = visible

    def set_enabled(self, field: Field, enabled: bool) -> None:
        field_set = self.__field_sets.get(field, None)
        if field_set is not None:
            field_set.enabled = enabled

    def set_disabled(self, field: Field, disabled: bool) -> None:
        self.set_enabled(field, not disabled)
