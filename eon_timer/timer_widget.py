from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QLabel, QGridLayout

from .app_state import AppState
from .resources import fonts
from .util import pyside
from .util.injector import component


@component()
class TimerWidget(QGroupBox):
    def __init__(self, state: AppState):
        super().__init__()
        self.state: Final[AppState] = state
        self.current_phase_lbl: Final[QLabel] = QLabel()
        self.minutes_before_target_lbl: Final[QLabel] = QLabel()
        self.next_phase_lbl: Final[QLabel] = QLabel()
        self.__init_components()

    def __init_components(self) -> None:
        layout = QGridLayout(self)
        layout.setAlignment(Qt.AlignTop)
        layout.setSpacing(5)

        # ----- current_phase_lbl -----
        self.current_phase_lbl.setText('0:000')
        layout.addWidget(self.current_phase_lbl, 0, 0, 1, 2)
        self.current_phase_lbl.setObjectName('currentPhaseLbl')
        font_name = fonts.resource_filename('RobotoMono-Regular.ttf')
        self.current_phase_lbl.setFont(pyside.get_font(font_name, 36))
        self.state.current_phase_changed.connect(self.__on_current_phase_changed)
        self.state.current_phase_elapsed_changed.connect(self.__on_current_phase_changed)
        # ----- minutes_before_target_lbl -----
        self.minutes_before_target_lbl.setText('0')
        layout.addWidget(QLabel('Minutes Before Target:'), 1, 0, Qt.AlignmentFlag.AlignRight)
        layout.addWidget(self.minutes_before_target_lbl, 1, 1, Qt.AlignmentFlag.AlignLeft)
        self.state.minutes_before_target_changed.connect(self.__on_minutes_before_target_changed)
        # ----- next_phase_lbl -----
        self.next_phase_lbl.setText('0:000')
        layout.addWidget(QLabel('Next Phase:'), 2, 0, Qt.AlignmentFlag.AlignRight)
        layout.addWidget(self.next_phase_lbl, 2, 1, Qt.AlignmentFlag.AlignLeft)
        self.state.next_phase_changed.connect(self.__on_next_phase_changed)

    def __on_current_phase_changed(self):
        current_phase = self.state.current_phase
        current_phase_elapsed = self.state.current_phase_elapsed
        text = self.__format_time(current_phase - current_phase_elapsed)
        self.current_phase_lbl.setText(text)

    def __on_minutes_before_target_changed(self, new_value: int):
        self.minutes_before_target_lbl.setText(str(new_value))

    def __on_next_phase_changed(self, new_value: int):
        self.next_phase_lbl.setText(self.__format_time(new_value))

    @staticmethod
    def __format_time(milliseconds: int | float) -> str:
        if isinstance(milliseconds, float):
            milliseconds = int(milliseconds)

        seconds = milliseconds // 1000
        milliseconds_part = milliseconds % 1000
        return f'{seconds}:{milliseconds_part:03d}'
