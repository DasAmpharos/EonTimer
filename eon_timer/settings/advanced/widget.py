from typing import Final

from PySide6.QtCore import Signal
from PySide6.QtGui import Qt
from PySide6.QtWidgets import QWidget, QPushButton, QVBoxLayout, QMessageBox

from eon_timer.util.injector import component
from eon_timer.util.pyside.name_service import NameService


@component()
class AdvancedSettingsWidget(QWidget):
    on_reset: Final[Signal] = Signal()

    def __init__(self, name_service: NameService):
        super().__init__()
        self.name_service: Final = name_service
        self.__init_components()

    def __init_components(self):
        self.name_service.set_name(self, 'advancedSettingsWidget')
        # ----- layout -----
        layout = QVBoxLayout(self)
        layout.setSpacing(10)
        # ----- reset_button -----
        button = QPushButton('Reset Settings')
        self.name_service.set_name(button, 'advancedSettingsResetButton')
        button.clicked.connect(self.__on_reset)
        layout.addWidget(button, alignment=Qt.AlignmentFlag.AlignTop)

    def __on_reset(self):
        reply = QMessageBox.warning(self,
                                    'Warning',
                                    'Are you sure you want to reset all settings? This operation cannot be undone.',
                                    QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No,
                                    QMessageBox.StandardButton.No)
        if reply == QMessageBox.StandardButton.Yes:
            self.on_reset.emit()
