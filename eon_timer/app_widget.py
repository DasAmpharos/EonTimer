from typing import Optional, Final

from PySide6.QtGui import QFontDatabase, QFont
from PySide6.QtWidgets import *

from . import util
from .app_config import AppConfig
from .gen3 import Gen3Widget
from .gen4 import Gen4Widget
from .gen5 import Gen5Widget
from .resources import fonts
from .settings.settings_dialog import SettingsDialog
from .timer_widget import TimerWidget


class AppWidget(QWidget):
    def __init__(self,
                 config: AppConfig,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.config: Final[AppConfig] = config
        self.timer_widget: Final[TimerWidget] = TimerWidget()
        self.gen5_widget: Final[Gen5Widget] = Gen5Widget(config.gen5)
        self.gen4_widget: Final[Gen4Widget] = Gen4Widget(config.gen4)
        self.gen3_widget: Final[Gen3Widget] = Gen3Widget(config.gen3)
        self.__init_components()

    def __init_components(self) -> None:
        # ----- root_layout -----
        root_layout = QGridLayout(self)
        root_layout.setColumnMinimumWidth(0, 215)
        root_layout.setHorizontalSpacing(10)
        root_layout.setVerticalSpacing(10)
        # ----- timer_display -----
        timer_display = TimerWidget()
        root_layout.addWidget(timer_display, 0, 0)
        timer_display.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        util.set_class(timer_display, ['themeable-panel', 'themeable-border'])
        timer_display.setObjectName('timerDisplay')
        # ----- tab_widget -----
        tab_widget = QTabWidget()
        root_layout.addWidget(tab_widget, 0, 1, 2, 2)
        util.set_class(tab_widget, ['themeable-panel', 'themeable-border'])
        tab_widget.addTab(self.gen5_widget, '5')
        tab_widget.addTab(self.gen4_widget, '4')
        tab_widget.addTab(self.gen3_widget, '3')
        # ----- settings_btn -----
        button = QPushButton()
        root_layout.addWidget(button, 2, 0)
        button.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        fontname = fonts.resource_filename('FontAwesome.ttf')
        application_font = QFontDatabase.addApplicationFont(fontname)
        font_families = QFontDatabase.applicationFontFamilies(application_font)
        font_family = next(iter(font_families))
        button.setFont(QFont(font_family))
        button.setText(chr(0xf013))
        button.clicked.connect(self.__show_settings_dialog)
        # ----- timer_btn -----
        button = QPushButton('Start')
        root_layout.addWidget(button, 2, 1)
        # ----- update_btn -----
        button = QPushButton('Update')
        root_layout.addWidget(button, 2, 2)

    def __show_settings_dialog(self) -> None:
        dialog = SettingsDialog(self.config.settings, self)
        if dialog.exec() == QDialog.DialogCode.Accepted:
            print('updating settings')
