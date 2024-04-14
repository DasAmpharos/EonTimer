from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QCheckBox, QSpinBox

from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from .model import TimerSettingsModel, Console


@component()
class TimerSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CONSOLE = 'Console'
        REFRESH_INTERVAL = 'Refresh Interval'
        PRECISION_CALIBRATION = 'Precision Calibration'

    def __init__(self, model: TimerSettingsModel) -> None:
        super().__init__()
        self.console: Final = Property(model.console.get())
        self.precision_calibration: Final = Property(model.precision_calibration.get())
        self.refresh_interval: Final = Property(model.refresh_interval.get())
        self.model: Final[TimerSettingsModel] = model
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- console -----
        field = EnumComboBox(Console)
        bindings.bind_combobox(field, self.console)
        self.add_field(self.Field.CONSOLE, field)
        # ----- refresh interval -----
        field = QSpinBox()
        bindings.bind_spinbox(field, self.refresh_interval)
        self.add_field(self.Field.REFRESH_INTERVAL, field)
        # ----- precision calibration -----
        field = QCheckBox()
        field.setTristate(False)
        bindings.bind_checkbox(field, self.precision_calibration)
        self.add_field(self.Field.PRECISION_CALIBRATION, field)

    def __init_listeners(self):
        pass

    def on_accepted(self):
        self.model.console.update(self.console)
        self.model.refresh_interval.update(self.refresh_interval)
        self.model.precision_calibration.update(self.precision_calibration)

    def on_rejected(self):
        self.console.update(self.model.console)
        self.refresh_interval.update(self.model.refresh_interval)
        self.precision_calibration.update(self.model.precision_calibration)
