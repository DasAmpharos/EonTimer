from abc import abstractmethod
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
        self.layout: Final[FormLayout] = FormLayout(self)
        self.field_sets: Final[dict[typename.Field, FieldSet]] = {}
        self._init_components()

    @abstractmethod
    def _init_components(self) -> None:
        pass

    def add_field(self,
                  field: Field,
                  widget: QWidget,
                  with_label: bool = True,
                  layout: Optional[FormLayout] = None) -> FieldSet:
        layout = layout or self.layout
        label = QLabel(str(field)) if with_label else None

        row = layout.add_row(widget, label)
        field_set = FieldSet(row, widget, label, layout)
        self.field_sets[field] = field_set
        return field_set

    def set_visible(self, field: Field, visible: bool) -> None:
        field_set = self.field_sets.get(field, None)
        if field_set is not None:
            field_set.visible = visible
