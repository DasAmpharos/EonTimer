from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QDialog, QGridLayout, QPushButton, QTabWidget)

from eon_timer import component
from eon_timer.settings.action.widget import ActionSettingsWidget
from eon_timer.settings.timer.widget import TimerSettingsWidget


@component()
class SettingsDialog(QDialog):
    def __init__(self,
                 action_settings_widget: ActionSettingsWidget,
                 timer_settings_widget: TimerSettingsWidget) -> None:
        super().__init__()
        self.action_settings_widget: Final[ActionSettingsWidget] = action_settings_widget
        self.timer_settings_widget: Final[TimerSettingsWidget] = timer_settings_widget
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('Preferences')
        self.setWindowFlags(Qt.Dialog |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint)
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setSpacing(10)
        # ----- tabs -----
        tabs = QTabWidget()
        tabs.addTab(self.action_settings_widget, 'Action')
        tabs.addTab(self.timer_settings_widget, 'Timer')
        layout.addWidget(tabs, 0, 0, 1, 2)
        # ----- cancel button -----
        button = QPushButton('Cancel')
        button.clicked.connect(self.__on_cancelled)
        layout.addWidget(button, 1, 0)
        # ----- ok button -----
        button = QPushButton('OK')
        button.clicked.connect(self.__on_accepted)
        layout.addWidget(button, 1, 1)
        button.setDefault(True)

    def __on_accepted(self) -> None:
        self.action_settings_widget.on_accepted()
        self.timer_settings_widget.on_accepted()
        self.done(QDialog.DialogCode.Accepted)

    def __on_cancelled(self) -> None:
        self.action_settings_widget.on_rejected()
        self.timer_settings_widget.on_rejected()
        self.done(QDialog.DialogCode.Rejected)
