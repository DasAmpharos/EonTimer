import functools
from typing import Final

from PySide6.QtCore import Signal
from PySide6.QtGui import QColor, QIcon, QPixmap
from PySide6.QtWidgets import QColorDialog, QPushButton

from eon_timer.util import const
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.file_selector_widget import FileSelectorWidget
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import IntInputField

from .model import ActionMode, ActionSettingsModel, ActionSound


class ActionSettingsWidget(FormWidget):
    test_action: Final = Signal()

    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        CUSTOM_SOUND = 'Custom Sound'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def __init__(self, name_service: NameService, model: ActionSettingsModel) -> None:
        super().__init__(name_service)
        self.model: Final = model
        self._color: QColor = model.color.get()
        self.__init_components()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'actionSettingsWidget')
        # ----- mode -----
        self._mode_field = EnumComboBox(ActionMode, self.model.mode.get())
        self._mode_field.setToolTip('Whether to use an audio cue, visual flash, or both')
        self.add_field(self.Field.MODE, self._mode_field, name='actionSettingsMode')
        # ----- sound -----
        self._sound_field = EnumComboBox(ActionSound, self.model.sound.get())
        self._sound_field.setToolTip('Built-in sound to play when the action fires')
        self._sound_field.value_changed.connect(self.__on_sound_changed)
        self.add_field(self.Field.SOUND, self._sound_field, name='actionSettingsSound')
        # ----- custom_sound -----
        self._custom_sound_field = FileSelectorWidget(title='Select Sound', filter='Sound Files (*.wav *.mp3)')
        self._custom_sound_field.file = self.model.custom_sound.get()
        self._custom_sound_field.setToolTip('Path to a custom .wav or .mp3 file to play instead of a built-in sound')
        self.add_field(
            self.Field.CUSTOM_SOUND,
            self._custom_sound_field,
            visible=self.model.sound.get() == ActionSound.CUSTOM,
            name='actionSettingsCustomSound',
        )
        # ----- color -----
        self._color_button = QPushButton()
        self._color_button.clicked.connect(functools.partial(self.__on_color_clicked, self._color_button))
        self._color_button.setToolTip('Color used for the visual flash overlay')
        self.add_field(self.Field.COLOR, self._color_button, name='actionSettingsColor')
        self.__set_icon_color(self._color_button)
        # ----- interval -----
        self._interval_field = IntInputField(value=self.model.interval.get(), min_val=0, max_val=const.INT_MAX, tooltip='Milliseconds between consecutive action cues (e.g. 500 = two cues 500 ms apart)')
        self.add_field(self.Field.INTERVAL, self._interval_field, name='actionSettingsInterval')
        # ----- count -----
        self._count_field = IntInputField(value=self.model.count.get(), min_val=0, max_val=const.INT_MAX, tooltip='Number of times the action repeats before the phase ends (set 1 for a single cue)')
        self.add_field(self.Field.COUNT, self._count_field, name='actionSettingsCount')
        # ----- test button -----
        test_btn = QPushButton('Test Action')
        test_btn.setToolTip('Fire the current action once to preview the sound/visual settings')
        self.name_service.set_name(test_btn, 'actionSettingsTestButton')
        test_btn.clicked.connect(self.test_action.emit)
        self._layout.add_row(test_btn)

    def __on_sound_changed(self, sound: ActionSound) -> None:
        self.set_visible(self.Field.CUSTOM_SOUND, sound == ActionSound.CUSTOM)

    def __on_color_clicked(self, button: QPushButton) -> None:
        color = QColorDialog.getColor(self._color, self)
        if color.isValid():
            self._color = color
            self.__set_icon_color(button)

    def __set_icon_color(self, button: QPushButton) -> None:
        pixmap = QPixmap(64, 64)
        pixmap.fill(self._color)
        button.setIcon(QIcon(pixmap))

    def on_accepted(self):
        self.model.mode.set(self._mode_field.get_value())
        self.model.sound.set(self._sound_field.get_value())
        self.model.custom_sound.set(self._custom_sound_field.file)
        self.model.color.set(self._color)
        self.model.interval.set(self._interval_field.value.get())
        self.model.count.set(self._count_field.value.get())
        self.model.settings_changed.emit()

    def on_rejected(self):
        self._mode_field.set_value(self.model.mode.get())
        self._sound_field.set_value(self.model.sound.get())
        self._custom_sound_field.file = self.model.custom_sound.get()
        self._color = self.model.color.get()
        self.__set_icon_color(self._color_button)
        self._interval_field.value.set(self.model.interval.get())
        self._count_field.value.set(self.model.count.get())

    def on_reset(self):
        self.model.reset()
        self.on_rejected()
