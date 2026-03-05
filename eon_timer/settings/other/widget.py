from typing import Final

from PySide6.QtWidgets import QCheckBox

from eon_timer.settings.other.update_model import UpdateSettingsModel
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService


class OtherSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CHECK_FOR_UPDATES = 'Check for Updates on Startup'

    def __init__(self, update_settings: UpdateSettingsModel, name_service: NameService):
        FormWidget.__init__(self, name_service)
        self.update_settings: Final = update_settings
        self.__init_components()

    @log_method_calls()
    def __init_components(self):
        self.name_service.set_name(self, 'otherSettingsWidget')
        # ----- check_for_updates -----
        self._check_on_startup_field = QCheckBox()
        self._check_on_startup_field.setChecked(self.update_settings.check_on_startup.get())
        self.add_field(self.Field.CHECK_FOR_UPDATES, self._check_on_startup_field, name='checkForUpdates')

    def on_accepted(self):
        self.update_settings.check_on_startup.set(self._check_on_startup_field.isChecked())
        self.update_settings.settings_changed.emit()

    def on_rejected(self):
        self._check_on_startup_field.setChecked(self.update_settings.check_on_startup.get())

    def on_reset(self):
        self.update_settings.reset()
        self.on_rejected()
