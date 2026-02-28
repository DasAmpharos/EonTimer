from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QCheckBox

from eon_timer.settings.other.update_model import UpdateSettingsModel
from eon_timer.util.const import INT_MAX
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import FloatInputField, IntInputField

from .model import Console, TimerSettingsModel


class TimerSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CONSOLE = 'Console'
        CUSTOM_FRAMERATE = 'Custom Framerate (FPS)'
        REFRESH_INTERVAL = 'Refresh Interval'
        MINIMUM_LENGTH = 'Minimum Length (s)'
        PRECISION_CALIBRATION = 'Precision Calibration'
        CHECK_FOR_UPDATES = 'Check for Updates on Startup'

    def __init__(self, name_service: NameService, model: TimerSettingsModel, update_settings: UpdateSettingsModel) -> None:
        super().__init__(name_service)
        self.model: Final = model
        self.update_settings: Final = update_settings
        self.__init_components()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'timerSettingsWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- console -----
        self._console_field = EnumComboBox(Console, self.model.console.get())
        self._console_field.value_changed.connect(self.__on_console_changed)
        self._console_field.setToolTip('Select your game console to use the correct framerate')
        self.add_field(self.Field.CONSOLE, self._console_field, name='timerSettingsConsole')
        # ----- custom framerate -----
        self._custom_framerate_field = FloatInputField(value=self.model.custom_framerate.get())
        self._custom_framerate_field.set_range(0.001, INT_MAX)
        self._custom_framerate_field.setToolTip('Frames per second for the "Custom" console option (must be > 0)')
        self.add_field(
            self.Field.CUSTOM_FRAMERATE,
            self._custom_framerate_field,
            visible=self.model.console.get() == Console.CUSTOM,
            name='timerSettingsCustomFramerate',
        )
        # ----- refresh interval -----
        self._refresh_interval_field = IntInputField(value=self.model.refresh_interval.get())
        self._refresh_interval_field.set_range(1, INT_MAX)
        self._refresh_interval_field.setToolTip('How often (in ms) the timer display updates — lower is smoother but uses more CPU')
        self.add_field(self.Field.REFRESH_INTERVAL, self._refresh_interval_field, name='timerSettingsRefreshInterval')
        # ----- minimum length -----
        self._minimum_length_field = IntInputField(value=self.model.minimum_length.get())
        self._minimum_length_field.set_range(0, INT_MAX)
        self._minimum_length_field.setToolTip('Minimum total timer duration in seconds before a minute is added; '
                                              'reduce this if your target falls naturally within a short window (e.g. ~8s on DS/Lite)')
        self.add_field(self.Field.MINIMUM_LENGTH, self._minimum_length_field, name='timerSettingsMinimumLength')
        # ----- precision calibration -----
        self._precision_field = QCheckBox()
        self._precision_field.setTristate(False)
        self._precision_field.setChecked(self.model.precision_calibration.get())
        self._precision_field.setToolTip('Apply a sub-frame precision adjustment to improve calibration accuracy')
        self.add_field(self.Field.PRECISION_CALIBRATION, self._precision_field, name='timerSettingsPrecisionCalibration')
        # ----- check for updates -----
        self._check_on_startup_field = QCheckBox()
        self._check_on_startup_field.setTristate(False)
        self._check_on_startup_field.setChecked(self.update_settings.check_on_startup.get())
        self._check_on_startup_field.setToolTip('Automatically check for a new EonTimer release when the application starts')
        self.add_field(self.Field.CHECK_FOR_UPDATES, self._check_on_startup_field, name='timerSettingsCheckForUpdates')

    def __on_console_changed(self, console: Console):
        self.set_visible(self.Field.CUSTOM_FRAMERATE, console == Console.CUSTOM)

    def on_accepted(self):
        self.model.console.set(self._console_field.get_value())
        self.model.custom_framerate.set(self._custom_framerate_field.value.get())
        self.model.refresh_interval.set(self._refresh_interval_field.value.get())
        self.model.minimum_length.set(self._minimum_length_field.value.get())
        self.model.precision_calibration.set(self._precision_field.isChecked())
        self.update_settings.check_on_startup.set(self._check_on_startup_field.isChecked())
        self.update_settings.settings_changed.emit()

    def on_rejected(self):
        self._console_field.set_value(self.model.console.get())
        self._custom_framerate_field.value.set(self.model.custom_framerate.get())
        self._refresh_interval_field.value.set(self.model.refresh_interval.get())
        self._minimum_length_field.value.set(self.model.minimum_length.get())
        self._precision_field.setChecked(self.model.precision_calibration.get())
        self._check_on_startup_field.setChecked(self.update_settings.check_on_startup.get())

    def on_reset(self):
        self.model.reset()
        self.update_settings.reset()
        self.on_rejected()
