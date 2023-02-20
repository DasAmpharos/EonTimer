from PySide6.QtCore import Qt
from PySide6.QtGui import QColor, QIcon, QPixmap
from PySide6.QtWidgets import QCheckBox, QComboBox, QSpinBox, QWidget

from .. import util
from ..form_widget import FormWidget


class TimerSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        CONSOLE = 'Console'
        REFRESH_INTERVAL = 'Refresh Interval'
        PRECISION_CALIBRATION = 'Precision Calibration'

    def _init_components(self) -> None:
        QWidget.set_class = util.set_class
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- console -----
        field = QComboBox()
        self._add_field(self.Field.CONSOLE, field)
        # ----- refresh interval -----
        field = QSpinBox()
        self._add_field(self.Field.REFRESH_INTERVAL, field)
        # ----- precision calibration -----
        field = QCheckBox()
        self._add_field(self.Field.PRECISION_CALIBRATION, field)
        field.setTristate(False)
