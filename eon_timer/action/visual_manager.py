from typing import Final

from PySide6.QtCore import QTimer, QObject
from PySide6.QtGui import QColor

from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.timer_widget import TimerWidget
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class VisualManager(QObject):
    def __init__(self,
                 timer_widget: TimerWidget,
                 action_settings: ActionSettingsModel):
        super().__init__(None)
        self.__active = False
        self.__timer: Final[QTimer] = QTimer(self)
        self.__timer.timeout.connect(self.deactivate)
        self.__inactive_style: Final[str] = timer_widget.styleSheet()
        self.__active_style = self.__get_active_style(action_settings.color.get())
        action_settings.color.on_change(self.__on_color_changed)
        self.timer_widget: Final[TimerWidget] = timer_widget

    def activate(self):
        if not self.__active:
            self.__active = True
            self.timer_widget.setStyleSheet(self.__active_style)
            self.__timer.start(50)

    def deactivate(self):
        if self.__active:
            self.__active = False
            self.timer_widget.setStyleSheet(self.__inactive_style)

    def __on_color_changed(self, event: PropertyChangeEvent[QColor]):
        self.__active_style = self.__get_active_style(event.new_value)

    def __get_active_style(self, color: QColor):
        color_style = f'rgba({color.red()}, {color.green()}, {color.blue()}, {color.alpha()})'
        return self.__inactive_style + f'background-color: {color_style};'
