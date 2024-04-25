import importlib.resources
from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QLabel, QVBoxLayout, QHBoxLayout, QWidget

from eon_timer.app_state import AppState
from eon_timer.util import pyside
from eon_timer.util.injector import component


@component()
class TimerWidget(QGroupBox):
    def __init__(self, state: AppState):
        super().__init__()
        self.state: Final[AppState] = state
        self.current_phase_lbl: Final[QLabel] = QLabel('0:000')
        self.minutes_before_target_lbl: Final[QLabel] = QLabel('0')
        self.next_phase_lbl: Final[QLabel] = QLabel('0:000')
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        layout = QVBoxLayout(self)
        layout.setSpacing(5)
        # ----- current_phase_lbl -----
        self.current_phase_lbl.setObjectName('currentPhaseLbl')
        layout.addWidget(self.current_phase_lbl, alignment=Qt.AlignmentFlag.AlignCenter)
        font_data = importlib.resources.read_binary('eon_timer.resources.fonts', 'RobotoMono-Regular.ttf')
        self.current_phase_lbl.setFont(pyside.get_font(font_data, 36))
        # ----- minutes_before_target_lbl -----
        group = QWidget()
        group_layout = QHBoxLayout(group)
        group_layout.setSpacing(5)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.addWidget(QLabel('Minutes Before Target:'), alignment=Qt.AlignmentFlag.AlignRight)
        group_layout.addWidget(self.minutes_before_target_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignCenter)
        # ----- next_phase_lbl -----
        group = QWidget()
        group_layout = QHBoxLayout(group)
        group_layout.setSpacing(5)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.addWidget(QLabel('Next Phase:'), alignment=Qt.AlignmentFlag.AlignRight)
        group_layout.addWidget(self.next_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignCenter)

    def __init_listeners(self):
        self.state.current_phase_changed.connect(self.__on_current_phase_changed)
        self.state.current_phase_elapsed_changed.connect(self.__on_current_phase_changed)
        self.state.minutes_before_target_changed.connect(self.__on_minutes_before_target_changed)
        self.state.next_phase_changed.connect(self.__on_next_phase_changed)

    def __on_current_phase_changed(self):
        current_phase = self.state.current_phase
        current_phase_elapsed = self.state.current_phase_elapsed
        text = self.__format_time(current_phase - current_phase_elapsed)
        self.current_phase_lbl.setText(text)

    def __on_minutes_before_target_changed(self, new_value: int):
        self.minutes_before_target_lbl.setText(str(new_value))

    def __on_next_phase_changed(self, new_value: float):
        self.next_phase_lbl.setText(self.__format_time(new_value))

    @staticmethod
    def __format_time(milliseconds: int | float) -> str:
        if isinstance(milliseconds, float):
            milliseconds = int(milliseconds)

        seconds = milliseconds // 1000
        milliseconds_part = milliseconds % 1000
        return f'{seconds}:{milliseconds_part:03d}'
