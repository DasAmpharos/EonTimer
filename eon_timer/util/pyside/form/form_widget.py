from enum import StrEnum
from typing import Final, Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QLabel, QSizePolicy, QWidget

from eon_timer.util import pyside
from eon_timer.util.pyside.name_service import NameService

from .field_set import FieldSet
from .form_layout import FormLayout


class FormWidget(QWidget):
    class Field(StrEnum):
        pass

    def __init__(self, name_service: NameService | None = None, parent: QWidget | None = None) -> None:
        QWidget.__init__(self, parent)

        typename = type(self)
        self._layout: Final = FormLayout(self)
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        self.__field_sets: Final[dict[typename.Field, FieldSet]] = {}
        self.name_service: Final = name_service

    def add_field(
        self,
        field: Field,
        widget: QWidget,
        with_label: bool = True,
        layout: Optional[FormLayout] = None,
        visible: bool = True,
        name: str | None = None,
    ) -> FieldSet:
        layout = layout or self._layout
        label = QLabel(str(field)) if with_label else None
        if name is not None and self.name_service is not None:
            if label is not None:
                self.name_service.set_name(label, f'{name}Label')
            self.name_service.set_name(widget, f'{name}Field')

        row = layout.add_row(widget, label)
        field_set = FieldSet(row, widget, label, layout)
        self.__field_sets[field] = field_set
        field_set.visible = visible
        return field_set

    def add_bound_field(
        self,
        field: Field,
        widget: QWidget,
        prop,
        with_label: bool = True,
        layout: Optional[FormLayout] = None,
        visible: bool = True,
        name: str | None = None,
    ) -> FieldSet:
        """Add a field and bidirectionally bind its `.value` property to `prop`."""
        from eon_timer.util.properties import bindings
        result = self.add_field(field, widget, with_label=with_label, layout=layout, visible=visible, name=name)
        bindings.bind(widget.value, prop)
        return result

    def _add_form_group(self, group_name: str) -> tuple['QGroupBox', FormLayout]:
        """Add a themed, expanding QGroupBox to self._layout and return it with its FormLayout."""
        group = QGroupBox()
        if self.name_service is not None:
            self.name_service.set_name(group, group_name)
        self._layout.add_row(group)
        form_layout = FormLayout(group)
        form_layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        pyside.set_class(group, ['themeable-panel', 'themeable-border'])
        group.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding)
        return group, form_layout

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
