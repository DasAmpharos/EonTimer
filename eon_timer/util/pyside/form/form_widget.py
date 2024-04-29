from enum import StrEnum
from typing import Final, Optional

from PySide6.QtWidgets import QLabel, QWidget

from .field_set import FieldSet
from .form_layout import FormLayout


class FormWidget(QWidget):
    class Field(StrEnum):
        pass

    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        typename = type(self)
        self._layout: Final[FormLayout] = FormLayout(self)
        self.__field_sets: Final[dict[typename.Field, FieldSet]] = {}

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
            widget.setObjectName(f'{name}Field')
            if label is not None:
                label.setObjectName(f'{name}Label')

        row = layout.add_row(widget, label)
        field_set = FieldSet(row, widget, label, layout)
        self.__field_sets[field] = field_set
        field_set.visible = visible
        return field_set

    def set_visible(self, field: Field, visible: bool) -> None:
        field_set = self.__field_sets.get(field, None)
        if field_set is not None:
            field_set.visible = visible
