from typing import Optional

from PySide6.QtWidgets import *

from timers.gen3 import TimerWidget as Gen3TimerWidget
from timers.gen4 import TimerWidget as Gen4TimerWidget
from util import set_class


class AppWidget(QWidget):
    def __init__(self,
                 parent: Optional[QWidget] = None) -> None:
        super().__init__(parent=parent)
        self.gen4_timer_widget = Gen4TimerWidget()
        self.gen3_timer_widget = Gen3TimerWidget()
        self.__init_components()

    def __init_components(self) -> None:
        QWidget.set_class = set_class
        layout = QGridLayout(parent=self)
        layout.setColumnMinimumWidth(0, 215)
        layout.setHorizontalSpacing(10)
        layout.setVerticalSpacing(10)
        # ----- tab_widget -----
        tab_widget = QTabWidget()
        tab_widget.set_class(['themeable-panel', 'themeable-border'])
        layout.addWidget(tab_widget, 0, 1, 2, 2)
        tab_widget.addTab(self.gen4_timer_widget, '4')
        tab_widget.addTab(self.gen3_timer_widget, '3')
