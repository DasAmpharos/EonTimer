import functools
from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtGui import QIcon, QPixmap
from PySide6.QtWidgets import QColorDialog, QPushButton

from eon_timer.util import const
from eon_timer.util.injector import component
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.file_selector_widget import FileSelectorWidget
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.name_service import NameService
from eon_timer.util.pyside.numeric_input_field import IntInputField
from .model import ActionMode, ActionSettingsModel, ActionSound


@component()
class ActionSettingsWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        SOUND = 'Sound'
        COLOR = 'Color'
        CUSTOM_SOUND = 'Custom Sound'
        INTERVAL = 'Interval'
        COUNT = 'Count'

    def __init__(self,
                 name_service: NameService,
                 model: ActionSettingsModel) -> None:
        super().__init__(name_service)
        self.mode: Final = Property(model.mode.get())
        self.sound: Final = Property(model.sound.get())
        self.color: Final = Property(model.color.get())
        self.custom_sound: Final = Property(model.custom_sound.get(), str)
        self.interval: Final = Property(model.interval.get())
        self.count: Final = Property(model.count.get())
        self.model: Final = model
        self.__init_components()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'actionSettingsWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(ActionMode)
        self.add_field(self.Field.MODE, field, name='actionSettingsMode')
        bindings.bind_enum_combobox(field, self.mode)
        # ----- sound -----
        field = EnumComboBox(ActionSound)
        self.add_field(self.Field.SOUND, field, name='actionSettingsSound')
        bindings.bind_enum_combobox(field, self.sound)
        # ----- custom_sound -----
        field = FileSelectorWidget(title='Select Sound',
                                   filter='Sound Files (*.wav *.mp3)')
        self.add_field(self.Field.CUSTOM_SOUND, field,
                       visible=self.sound.get() == ActionSound.CUSTOM,
                       name='actionSettingsCustomSound')
        bindings.bind(field.file, self.custom_sound, True)
        self.sound.on_change(self.__on_sound_changed)
        # ----- color -----
        field = QPushButton()
        field.clicked.connect(functools.partial(self.__on_color_clicked, field))
        self.add_field(self.Field.COLOR, field, name='actionSettingsColor')
        self.__set_icon_color(field)
        # ----- interval -----
        field = IntInputField()
        field.set_range(0, const.INT_MAX)
        bindings.bind(field.value, self.interval)
        self.add_field(self.Field.INTERVAL, field, name='actionSettingsInterval')
        # ----- count -----
        field = IntInputField()
        field.set_range(0, const.INT_MAX)
        bindings.bind(field.value, self.count)
        self.add_field(self.Field.COUNT, field, name='actionSettingsCount')

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
