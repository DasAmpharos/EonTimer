from typing import Optional

from eon_timer.gui import util
from eon_timer.gui.timers.gen3 import TimerWidget as Gen3TimerWidget
from eon_timer.gui.timers.gen4 import TimerWidget as Gen4TimerWidget
from eon_timer.gui.timers.gen5 import TimerWidget as Gen5TimerWidget
from PySide6.QtWidgets import *

from .timer_display_widget import TimerDisplayWidget


class AppWidget(QWidget):
    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.gen5_timer_widget = Gen5TimerWidget()
        self.gen4_timer_widget = Gen4TimerWidget()
        self.gen3_timer_widget = Gen3TimerWidget()
        self.__init_components()

    def __init_components(self) -> None:
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
        # ----- timer_btn -----
        timer_btn = QPushButton('Start')
        root_layout.addWidget(timer_btn, 3, 1)
        # ----- update_btn -----
        update_btn = QPushButton('Update')
        root_layout.addWidget(update_btn, 3, 2)
