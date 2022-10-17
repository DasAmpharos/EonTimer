`import functools
import functools
from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import (QDialog, QGridLayout, QPushButton, QTabWidget,
                               QWidget)


class SettingsDialog(QDialog):
    def __init__(self, parent: Optional[QWidget] = ...) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        self.setWindowTitle('Preferences')
        self.setWindowFlags(functools.reduce(
            lambda f1, f2: f1 | f2,
            [
                Qt.Dialog,
                Qt.WindowTitleHint,
                Qt.CustomizeWindowHint,
                Qt.WindowCloseButtonHint
            ]))
        layout = QGridLayout(self)
        layout.setVerticalSpacing(10)

        tab_pane = QTabWidget()
        tab_pane.addTab(None, "Action")
        tab_pane.addTab(None, "Timer")
        layout.addWidget(tab_pane, 0, 0, 1, 2)

        ok_btn = QPushButton('OK')
        ok_btn.clicked.connect()

        cancel_btn = QPushButton('Cancel')
        cancel_btn.clicked.connect(lambda _: self.done(QDialog.Rejected))
