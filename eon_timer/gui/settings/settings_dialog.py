from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QDialog, QGridLayout, QPushButton, QTabWidget,
                               QWidget)

from .action_widget import ActionSettingsWidget
from .timer_widget import TimerSettingsWidget


class SettingsDialog(QDialog):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('Preferences')
        self.setWindowFlags(Qt.Dialog | Qt.WindowTitleHint |
                            Qt.CustomizeWindowHint | Qt.WindowCloseButtonHint)
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setSpacing(10)
        # ----- tabs -----
        tabs = QTabWidget()
        tabs.addTab(ActionSettingsWidget(), 'Action')
        tabs.addTab(TimerSettingsWidget(), 'Timer')
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
        self.done(QDialog.Accepted)

    def __on_cancelled(self) -> None:
        self.done(QDialog.Rejected)
