from typing import Final

from PySide6.QtCore import Qt

from eon_timer.util.injector import component
from eon_timer.util.properties.property import Property
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from .model import ThemeSettingsModel, Theme
from ...util.properties import bindings


@component()
class ThemeSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        THEME = 'Theme'

    def __init__(self, model: ThemeSettingsModel) -> None:
        super().__init__()
        self.theme: Final = Property(model.theme.get())
        self.model: Final[ThemeSettingsModel] = model
        self.__init_components()

    def __init_components(self):
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- theme -----
        field = EnumComboBox(Theme)
        bindings.bind_combobox(field, self.theme)
        self.add_field(self.Field.THEME, field)

    def on_accepted(self):
        self.model.theme.update(self.theme)

    def on_rejected(self):
        self.theme.update(self.model.theme)
