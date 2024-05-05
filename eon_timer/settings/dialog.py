from typing import Final

from PySide6.QtCore import QSettings, Qt
from PySide6.QtWidgets import (QDialog, QGridLayout, QPushButton, QTabWidget)

from eon_timer import component
from eon_timer.settings.action.widget import ActionSettingsWidget
from eon_timer.settings.advanced.widget import AdvancedSettingsWidget
from eon_timer.settings.theme.widget import ThemeSettingsWidget
from eon_timer.settings.timer.widget import TimerSettingsWidget
from eon_timer.util import pyside
from eon_timer.util.pyside.name_service import NameService


@component()
class SettingsDialog(QDialog):
    def __init__(self,
                 settings: QSettings,
                 name_service: NameService,
                 action_settings_widget: ActionSettingsWidget,
                 timer_settings_widget: TimerSettingsWidget,
                 theme_settings_widget: ThemeSettingsWidget,
                 advanced_settings_widget: AdvancedSettingsWidget) -> None:
        super().__init__()
        self.settings: Final = settings
        self.name_service: Final = name_service
        self.action_settings_widget: Final = action_settings_widget
        self.timer_settings_widget: Final = timer_settings_widget
        self.theme_settings_widget: Final = theme_settings_widget
        self.advanced_settings_widget: Final = advanced_settings_widget
        self.__init_components()

    def __init_components(self) -> None:
        self.name_service.set_name(self, 'settingsDialog')
        self.setWindowTitle('Settings')
        self.setWindowFlags(Qt.Dialog |
                            Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint |
                            Qt.WindowCloseButtonHint)
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setSpacing(10)
        # ----- tabs -----
        tabs = QTabWidget()
        self.name_service.set_name(tabs, 'settingsTabWidget')
        tabs.addTab(self.action_settings_widget, 'Action')
        tabs.addTab(self.timer_settings_widget, 'Timer')
        tabs.addTab(self.theme_settings_widget, 'Theme')
        tabs.addTab(self.advanced_settings_widget, 'Advanced')
        self.advanced_settings_widget.on_reset.connect(self.__on_reset)
        pyside.set_class(tabs, ['themeable-panel', 'themeable-border'])
        layout.addWidget(tabs, 0, 0, 1, 2)
        # ----- cancel button -----
        button = QPushButton('Cancel')
        self.name_service.set_name(button, 'settingsCancelButton')
        button.clicked.connect(self.__on_cancelled)
        layout.addWidget(button, 1, 0)
        # ----- ok button -----
        button = QPushButton('OK')
        self.name_service.set_name(button, 'settingsOkButton')
        button.clicked.connect(self.__on_accepted)
        layout.addWidget(button, 1, 1)
        button.setDefault(True)

    def __on_accepted(self) -> None:
        self.action_settings_widget.on_accepted()
        self.timer_settings_widget.on_accepted()
        self.theme_settings_widget.on_accepted()
        self.done(QDialog.DialogCode.Accepted)

    def __on_cancelled(self) -> None:
        self.action_settings_widget.on_rejected()
        self.timer_settings_widget.on_rejected()
        self.theme_settings_widget.on_rejected()
        self.done(QDialog.DialogCode.Rejected)

    def __on_reset(self):
        self.settings.clear()
        self.action_settings_widget.on_reset()
        self.timer_settings_widget.on_reset()
        self.theme_settings_widget.on_reset()
        self.done(QDialog.DialogCode.Accepted)
