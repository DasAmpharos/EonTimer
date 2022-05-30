from PySide6.QtWidgets import *
from .timers.gen3 import TimerWidget as Gen3TimerWidget
from .timers.gen4 import TimerWidget as Gen4TimerWidget


class AppWidget(QWidget):
    def __init__(self, parent: QWidget | None = None):
        super().__init__(parent)
        self.__init_components()

    def __init_components(self):
        layout = QGridLayout(self)
        layout.setColumnMinimumWidth(0, 215)
        layout.setHorizontalSpacing(10)
        layout.setVerticalSpacing(10)
        # ----- tab_widget -----
        tab_widget = QTabWidget()
        tab_widget.setProperty('class', ' '.join(['themeable-panel', 'themeable-border']))
        layout.addWidget(tab_widget, 0, 1, 2, 2)
        tab_widget.addTab(Gen4TimerWidget(), '4')
        tab_widget.addTab(Gen3TimerWidget(), '3')
        pass
