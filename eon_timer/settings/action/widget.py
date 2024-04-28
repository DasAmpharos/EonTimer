import functools
from typing import Final

import pygame
from PySide6.QtCore import Qt
from PySide6.QtGui import QIcon, QPixmap
from PySide6.QtWidgets import QPushButton, QSpinBox, QColorDialog

from eon_timer.util import const
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.file_selector_widget import FileSelectorWidget
from eon_timer.util.pyside.form import FormWidget
from .model import ActionSettingsModel, ActionMode, ActionSound


@component()
class ActionSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        CUSTOM_SOUND = 'Custom Sound'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def __init__(self, model: ActionSettingsModel) -> None:
        super().__init__()
        self.mode: Final = Property(model.mode.get())
        self.sound: Final = Property(model.sound.get())
        self.color: Final = Property(model.color.get())
        self.custom_sound: Final = Property(model.custom_sound.get(), str)
        self.interval: Final = Property(model.interval.get())
        self.count: Final = Property(model.count.get())
        self.model: Final[ActionSettingsModel] = model
        self.__init_components()

    def __init_components(self) -> None:
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(ActionMode)
        bindings.bind_enum_combobox(field, self.mode)
        self.add_field(self.Field.MODE, field)
        # ----- sound -----
        field = EnumComboBox(ActionSound)
        bindings.bind_enum_combobox(field, self.sound)
        self.add_field(self.Field.SOUND, field)
        # ----- custom_sound -----
        field = FileSelectorWidget(title='Select Sound',
                                   filter='Sound Files (*.wav *.mp3)',
                                   validator=self.__is_valid_sound)
        bindings.bind(field.file, self.custom_sound, True)
        self.add_field(self.Field.CUSTOM_SOUND, field, visible=self.sound.get() == ActionSound.CUSTOM)
        self.sound.on_change(self.__on_sound_changed)
        # ----- color -----
        field = QPushButton()
        field.clicked.connect(functools.partial(self.__on_color_clicked, field))
        self.add_field(self.Field.COLOR, field)
        self.__set_icon_color(field)
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

    def __on_sound_changed(self, event: PropertyChangeEvent[ActionSound]) -> None:
        self.set_visible(self.Field.CUSTOM_SOUND, event.new_value == ActionSound.CUSTOM)

    def __on_color_clicked(self, button: QPushButton) -> None:
        color = self.color.get()
        color = QColorDialog.getColor(color, self)
        if color.isValid():
            self.color.set(color)
            self.__set_icon_color(button)

    def __set_icon_color(self, button: QPushButton) -> None:
        pixmap = QPixmap(64, 64)
        pixmap.fill(self.color.get())
        button.setIcon(QIcon(pixmap))

    @staticmethod
    def __is_valid_sound(filepath: str) -> bool:
        try:
            pygame.mixer.Sound(filepath)
            return True
        except pygame.error:
            return False

    def on_accepted(self):
        self.model.mode.update(self.mode)
        self.model.sound.update(self.sound)
        self.model.custom_sound.update(self.custom_sound)
        self.model.color.update(self.color)
        self.model.interval.update(self.interval)
        self.model.count.update(self.count)
        self.model.settings_changed.emit()

    def on_rejected(self):
        self.__reset_properties()

    def on_reset(self):
        self.model.reset()
        self.__reset_properties()

    def __reset_properties(self):
        self.mode.update(self.model.mode)
        self.sound.update(self.model.sound)
        self.custom_sound.update(self.model.custom_sound)
        self.color.update(self.model.color)
        self.interval.update(self.model.interval)
        self.count.update(self.model.count)
