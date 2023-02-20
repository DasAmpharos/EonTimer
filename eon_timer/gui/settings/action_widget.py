from ..form_widget import FormWidget
from PySide6.QtCore import Qt
from PySide6.QtGui import QColor, QIcon, QPixmap
from PySide6.QtWidgets import QComboBox, QPushButton, QSpinBox, QWidget
from .. import util


class ActionSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def _init_components(self) -> None:
        QWidget.set_class = util.set_class
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = QComboBox()
        self._add_field(self.Field.MODE, field)
        # ----- sound -----
        field = QComboBox()
        self._add_field(self.Field.SOUND, field)
        # ----- color -----
        field = QPushButton()
        self._add_field(self.Field.COLOR, field)
        self.__set_icon_color(field)
        # ----- interval -----
        field = QSpinBox()
        self._add_field(self.Field.INTERVAL, field)
        # ----- count -----
        field = QSpinBox()
        self._add_field(self.Field.COUNT, field)
    
    def __set_icon_color(self, button: QPushButton) -> None:
        pixmap = QPixmap(64, 64)
        color = QColor.fromRgb(0, 255, 255)
        pixmap.fill(color)
        icon = QIcon(pixmap)
        button.setIcon(icon)
