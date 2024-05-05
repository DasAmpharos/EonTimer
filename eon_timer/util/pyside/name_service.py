from typing import Final

from PySide6.QtWidgets import QWidget

from eon_timer.settings.theme.model import ThemeSettingsModel
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class NameService:
    def __init__(self, model: ThemeSettingsModel):
        self.model: Final = model
        self.__named: set[QWidget] = set()
        self.__tooltip_cache: dict[QWidget, str] = {}
        model.element_name_tooltip.on_change(self.__on_element_name_tooltip_changed)

    def set_name(self, widget: QWidget, name: str):
        widget.setObjectName(name)
        self.__tooltip_cache[widget] = widget.toolTip()
        if self.model.element_name_tooltip.get():
            widget.setToolTip(name)
        self.__named.add(widget)

    def get_name(self, widget: QWidget) -> str | None:
        if self.is_named(widget):
            return widget.objectName()
        return None

    def is_named(self, widget: QWidget) -> bool:
        return widget in self.__named

    def __on_element_name_tooltip_changed(self, event: PropertyChangeEvent[bool]):
        for named in self.__named:
            tooltip = self.__tooltip_cache.get(named, '')
            if event.new_value:
                tooltip = named.objectName()
            named.setToolTip(tooltip)
