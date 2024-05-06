from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QCheckBox

from eon_timer.settings.other.update_model import UpdateSettingsModel
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService


@component()
class OtherSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CHECK_FOR_UPDATES = 'Check for Updates on Startup'

    def __init__(self,
                 update_settings: UpdateSettingsModel,
                 name_service: NameService):
        FormWidget.__init__(self, name_service)
        self.update_settings: Final = update_settings
        self.check_on_startup: Final = Property(self.update_settings.check_on_startup.get())
        self.__init_components()

    def __init_components(self):
        self.name_service.set_name(self, 'otherSettingsWidget')
        # ---- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- check_for_updates -----
        field = QCheckBox()
        bindings.bind_checkbox(field, self.check_on_startup)
        self.add_field(self.Field.CHECK_FOR_UPDATES, field, name='checkForUpdates')

    def on_accepted(self):
        self.update_settings.check_on_startup.update(self.check_on_startup)
        self.update_settings.settings_changed.emit()

    def on_rejected(self):
        self.__reset_properties()

    def on_reset(self):
        self.update_settings.reset()
        self.__reset_properties()

    def __reset_properties(self):
        self.check_on_startup.update(self.update_settings.check_on_startup)
