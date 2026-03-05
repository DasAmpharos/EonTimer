from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QCheckBox

from eon_timer.util.const import INT_MAX
from eon_timer.util.injector import component
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import BoolProperty, FloatProperty, IntProperty, Property
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import FloatInputField, IntInputField
from .model import Console, TimerSettingsModel


@component()
class TimerSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CONSOLE = 'Console'
        CUSTOM_FRAMERATE = 'Custom Framerate'
        REFRESH_INTERVAL = 'Refresh Interval'
        MINIMUM_LENGTH = 'Minimum Length (s)'
        PRECISION_CALIBRATION = 'Precision Calibration'

    def __init__(self,
                 name_service: NameService,
                 model: TimerSettingsModel) -> None:
        super().__init__(name_service)
        self.console: Final = Property(model.console.get())
        self.custom_framerate: Final = FloatProperty(model.custom_framerate.get())
        self.precision_calibration: Final = BoolProperty(model.precision_calibration.get())
        self.refresh_interval: Final = Property(model.refresh_interval.get())
        self.minimum_length: Final = Property(model.minimum_length.get())
        self.model: Final = model
        self.__init_components()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'timerSettingsWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- console -----
        field = EnumComboBox(Console)
        self.add_field(self.Field.CONSOLE, field,
                       name='timerSettingsConsole')
        bindings.bind_enum_combobox(field, self.console)
        # ----- custom framerate -----
        field = FloatInputField()
        field.set_range(0, INT_MAX)
        bindings.bind(field.value, self.custom_framerate)
        self.add_field(self.Field.CUSTOM_FRAMERATE, field,
                       visible=self.console.get() == Console.CUSTOM,
                       name='timerSettingsCustomFramerate')
        self.console.on_change(self.__on_console_changed)
        # ----- refresh interval -----
        field = IntInputField()
        field.set_range(1, INT_MAX)
        bindings.bind(field.value, self.refresh_interval)
        self.add_field(self.Field.REFRESH_INTERVAL, field,
                       name='timerSettingsRefreshInterval')
        # ----- minimum length -----
        field = IntInputField()
        field.set_range(0, INT_MAX)
        field.setToolTip('Minimum total timer duration in seconds before a minute is added; '
                         'reduce this if your target falls naturally within a short window (e.g. ~8s on DS/Lite)')
        bindings.bind(field.value, self.minimum_length)
        self.add_field(self.Field.MINIMUM_LENGTH, field,
                       name='timerSettingsMinimumLength')
        # ----- precision calibration -----
        field = QCheckBox()
        self.add_field(self.Field.PRECISION_CALIBRATION, field,
                       name='timerSettingsPrecisionCalibration')
        field.setTristate(False)
        bindings.bind_checkbox(field, self.precision_calibration)

    def __on_console_changed(self, event: PropertyChangeEvent[Console]):
        self.set_visible(self.Field.CUSTOM_FRAMERATE, event.new_value == Console.CUSTOM)

    def on_accepted(self):
        self.model.console.update(self.console)
        self.model.custom_framerate.update(self.custom_framerate)
        self.model.refresh_interval.update(self.refresh_interval)
        self.model.minimum_length.update(self.minimum_length)
        self.model.precision_calibration.update(self.precision_calibration)

    def on_rejected(self):
        self.__reset_properties()

    def on_reset(self):
        self.model.reset()
        self.__reset_properties()

    def __reset_properties(self):
        self.console.update(self.model.console)
        self.custom_framerate.update(self.model.custom_framerate)
        self.refresh_interval.update(self.model.refresh_interval)
        self.minimum_length.update(self.model.minimum_length)
        self.precision_calibration.update(self.model.precision_calibration)
