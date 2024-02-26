from typing import Optional, Final, Callable

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QCheckBox, QComboBox, QSpinBox, QWidget

from .timer_config import TimerSettingsConfig
from .. import util
from ..console import Console
from ..util import FormWidget


class TimerSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CONSOLE = 'Console'
        REFRESH_INTERVAL = 'Refresh Interval'
        PRECISION_CALIBRATION = 'Precision Calibration'

    def __init__(self,
                 config: TimerSettingsConfig,
                 parent: Optional[QWidget] = None) -> None:
        self.__config: Final[TimerSettingsConfig] = config
        self.__changes: Final[TimerSettingsConfig] = config.model_copy()
        super().__init__(parent)

    def _init_components(self) -> None:
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- console -----
        field = QComboBox()
        self.add_field(self.Field.CONSOLE, field)
        util.add_items(field, Console, self.__changes.console)
        field.currentTextChanged.connect(self.__on_field_changed(self.Field.CONSOLE))
        # ----- refresh interval -----
        field = QSpinBox()
        self.add_field(self.Field.REFRESH_INTERVAL, field)
        field.valueChanged.connect(self.__on_field_changed(self.Field.REFRESH_INTERVAL))
        # ----- precision calibration -----
        field = QCheckBox()
        field.setTristate(False)
        self.add_field(self.Field.PRECISION_CALIBRATION, field)
        field.stateChanged.connect(self.__on_field_changed(self.Field.PRECISION_CALIBRATION))

    def __on_field_changed(self, field: Field) -> Callable[[any], None]:
        def implementation(new_value):
            match field:
                case self.Field.CONSOLE:
                    self.__changes.console = new_value
                case self.Field.REFRESH_INTERVAL:
                    self.__changes.refresh_interval = new_value
                case self.Field.PRECISION_CALIBRATION:
                    self.__changes.precision_calibration = new_value

        return implementation

    def on_accepted(self):
        changes = self.__changes.model_dump()
        print(changes)
        self.__config.__dict__.update(changes)
