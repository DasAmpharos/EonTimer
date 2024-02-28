from typing import Optional, Final, Callable

from PySide6.QtCore import Qt
from PySide6.QtGui import QIcon, QPixmap, QColor
from PySide6.QtWidgets import QComboBox, QPushButton, QSpinBox, QWidget, QColorDialog

from eon_timer.settings.action.action_color import ActionColor
from eon_timer.settings.action.action_mode import ActionMode
from eon_timer.settings.action.action_sound import ActionSound
from eon_timer.util import util, constants
from eon_timer.util.form_widget import FormWidget
from .config import ActionConfig


class ActionSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def __init__(self,
                 config: ActionConfig,
                 parent: Optional[QWidget] = None) -> None:
        self.config: Final[ActionConfig] = config
        self.changes: Final[ActionConfig] = config.model_copy()
        super().__init__(parent)

    def _init_components(self) -> None:
        # ----- layout -----
        self.layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self.layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = QComboBox()
        self.add_field(self.Field.MODE, field)
        util.add_items(field, ActionMode, self.config.mode)
        field.currentTextChanged.connect(self.__on_field_changed(self.Field.MODE))
        # ----- sound -----
        field = QComboBox()
        self.add_field(self.Field.SOUND, field)
        util.add_items(field, ActionSound, self.config.sound)
        field.currentTextChanged.connect(self.__on_field_changed(self.Field.SOUND))
        # ----- color -----
        field = QPushButton()
        self.add_field(self.Field.COLOR, field)
        field.clicked.connect(self.__on_color_clicked(field))
        self.__set_icon_color(field)
        # ----- interval -----
        field = QSpinBox()
        self.add_field(self.Field.INTERVAL, field)
        field.setRange(0, constants.INT_MAX)
        field.setValue(self.config.interval)
        field.valueChanged.connect(self.__on_field_changed(self.Field.INTERVAL))
        # ----- count -----
        field = QSpinBox()
        self.add_field(self.Field.COUNT, field)
        field.setRange(0, constants.INT_MAX)
        field.setValue(self.config.count)
        field.valueChanged.connect(self.__on_field_changed(self.Field.COUNT))

    def __on_field_changed(self, field: Field) -> Callable[[any], None]:
        def implementation(new_value):
            print(f'{field}={new_value}')

        return implementation

    def __on_color_clicked(self, button: QPushButton) -> Callable[[], None]:
        def implementation():
            color = self.changes.color.to_qcolor()
            color = QColorDialog.getColor(color, self)
            if color.isValid():
                self.changes.color = ActionColor.from_qcolor(color)
                self.__set_icon_color(button, color)

        return implementation

    def __set_icon_color(self,
                         button: QPushButton,
                         color: Optional[QColor] = None) -> None:
        pixmap = QPixmap(64, 64)
        pixmap.fill(color or self.changes.color.to_qcolor())
        button.setIcon(QIcon(pixmap))

    def on_accepted(self):
        changes = self.changes.model_dump()
        print(changes)
        self.config.__dict__.update(changes)

    def on_cancelled(self):
        pass
