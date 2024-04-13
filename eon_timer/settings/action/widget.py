from typing import Final, Callable

from PySide6.QtCore import Qt
from PySide6.QtGui import QIcon, QPixmap, QColor
from PySide6.QtWidgets import QPushButton, QSpinBox, QColorDialog

from eon_timer.util import const
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from .model import ActionSettingsModel, ActionMode, ActionSound


@component()
class ActionSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def __init__(self, model: ActionSettingsModel) -> None:
        super().__init__()
        self.mode: Final = Property(model.mode.get())
        self.sound: Final = Property(model.sound.get())
        self.color: Final = Property(model.color.get())
        self.interval: Final = Property(model.interval.get())
        self.count: Final = Property(model.count.get())
        self.model: Final[ActionSettingsModel] = model
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(ActionMode)
        bindings.bind_combobox(field, self.mode)
        self.add_field(self.Field.MODE, field)
        # ----- sound -----
        field = EnumComboBox(ActionSound)
        bindings.bind_combobox(field, self.sound)
        self.add_field(self.Field.SOUND, field)
        # ----- color -----
        field = QPushButton()
        self.__set_icon_color(field, self.color)
        field.clicked.connect(self.__on_color_clicked(field))
        self.add_field(self.Field.COLOR, field)
        # ----- interval -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.interval)
        self.add_field(self.Field.INTERVAL, field)
        # ----- count -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.count)
        self.add_field(self.Field.COUNT, field)

    def __init_listeners(self):
        pass

    def __on_color_clicked(self, button: QPushButton) -> Callable[[], None]:
        def implementation():
            color = self.color.get()
            color = QColorDialog.getColor(color, self)
            if color.isValid():
                self.color.set(color)
                self.__set_icon_color(button, color)

        return implementation

    @staticmethod
    def __set_icon_color(button: QPushButton,
                         color: Property[QColor]) -> None:
        pixmap = QPixmap(64, 64)
        pixmap.fill(color.get())
        button.setIcon(QIcon(pixmap))

    def on_accepted(self):
        self.model.mode.update(self.mode)
        self.model.sound.update(self.sound)
        self.model.color.update(self.color)
        self.model.interval.update(self.interval)
        self.model.count.update(self.count)

    def on_rejected(self):
        self.mode.update(self.model.mode)
        self.sound.update(self.model.sound)
        self.color.update(self.model.color)
        self.interval.update(self.model.interval)
        self.count.update(self.model.count)
