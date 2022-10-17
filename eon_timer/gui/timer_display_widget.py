from typing import Optional

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QHBoxLayout, QLabel, QVBoxLayout, QWidget


class TimerDisplayWidget(QGroupBox):
    def __init__(self, parent: Optional[QWidget] = None) -> None:
        super().__init__(parent)
        self.__init_components()

    def __init_components(self) -> None:
        root_layout = QVBoxLayout(self)
        root_layout.setAlignment(Qt.AlignTop)
        root_layout.setSpacing(5)
        # ----- current_stage -----
        field = QLabel('0:00')
        root_layout.addWidget(field)
        field.setObjectName('currentStageLbl')
        # ----- minutes_before_target -----
        field = QLabel('0')
        layout = QHBoxLayout()
        layout.setSpacing(5)
        root_layout.addLayout(layout)
        root_layout.setAlignment(layout, Qt.AlignLeft)
        layout.addWidget(QLabel('Minutes Before Target:'))
        layout.addWidget(field)
        # ----- next_stage -----
        field = QLabel('0:00')
        layout = QHBoxLayout()
        layout.setSpacing(5)
        root_layout.addLayout(layout)
        root_layout.setAlignment(layout, Qt.AlignLeft)
        layout.addWidget(QLabel('Next Stage:'))
        layout.addWidget(field)
