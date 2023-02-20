from typing import Optional

from eon_timer.resources import fonts
from PySide6.QtCore import Qt
from PySide6.QtGui import QFont, QFontDatabase
from PySide6.QtWidgets import *

<<<<<<< Updated upstream
from .timer_display_widget import TimerDisplayWidget
=======
from . import util
from .settings.settings_dialog import SettingsDialog
from .timer_display_widget import TimerDisplayWidget
from .timers.gen3 import TimerWidget as Gen3TimerWidget
from .timers.gen4 import TimerWidget as Gen4TimerWidget
from .timers.gen5 import TimerWidget as Gen5TimerWidget
>>>>>>> Stashed changes


class AppWidget(QWidget):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.timer_display = TimerDisplayWidget()
        self.gen5_timer = Gen5TimerWidget()
        self.gen4_timer = Gen4TimerWidget()
        self.gen3_timer = Gen3TimerWidget()
        self.__init_components()

    def __init_components(self) -> None:
<<<<<<< Updated upstream
        # ----- root_layout -----
        root_layout = QGridLayout(self)
        root_layout.setColumnMinimumWidth(0, 215)
        root_layout.setHorizontalSpacing(10)
        root_layout.setVerticalSpacing(10)
        # ----- timer_display -----
        timer_display = TimerDisplayWidget()
        root_layout.addWidget(timer_display, 0, 0)
        timer_display.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Fixed)
        util.set_class(timer_display, ['themeable-panel', 'themeable-border'])
        timer_display.setObjectName('timerDisplay')
        # ----- tab_widget -----
        tab_widget = QTabWidget()
        root_layout.addWidget(tab_widget, 0, 1, 2, 2)
        util.set_class(tab_widget, ['themeable-panel', 'themeable-border'])
        tab_widget.addTab(self.gen5_timer_widget, '5')
        tab_widget.addTab(self.gen4_timer_widget, '4')
        tab_widget.addTab(self.gen3_timer_widget, '3')
=======
        QWidget.set_class = util.set_class
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setColumnMinimumWidth(0, 215)
        layout.setHorizontalSpacing(10)
        layout.setVerticalSpacing(10)
        # ----- timer_display -----
        layout.addWidget(self.timer_display, 0, 0, Qt.AlignTop)
        self.timer_display.set_class(['themeable-panel', 'themeable-border'])
        self.timer_display.setSizePolicy(
            QSizePolicy.Expanding, QSizePolicy.Fixed)
        # ----- tab_widget -----
        tab_widget = QTabWidget()
        layout.addWidget(tab_widget, 0, 1, 1, 2)
        tab_widget.set_class(['themeable-panel', 'themeable-border'])
        tab_widget.addTab(self.gen5_timer, '5')
        tab_widget.addTab(self.gen4_timer, '4')
        tab_widget.addTab(self.gen3_timer, '3')
        # ----- settings_btn -----
        button = QPushButton()
        layout.addWidget(button, 2, 0)
        button.clicked.connect(self.__show_settings_dialog)
        button.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        fontname = fonts.resource_filename('FontAwesome.ttf')
        application_font = QFontDatabase.addApplicationFont(fontname)
        font_families = QFontDatabase.applicationFontFamilies(application_font)
        font_family = next(iter(font_families))
        button.setFont(QFont(font_family))
        button.setText(chr(0xf013))
>>>>>>> Stashed changes
        # ----- timer_btn -----
        button = QPushButton('Start')
        layout.addWidget(button, 2, 1)
        # ----- update_btn -----
        button = QPushButton('Update')
        layout.addWidget(button, 2, 2)

    def __show_settings_dialog(self) -> None:
        dialog = SettingsDialog(self)
        if dialog.exec() == QDialog.Accepted:
            print('updating settings')
