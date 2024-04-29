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
        self.setObjectName('timerWidget')
        # ----- layout -----
        layout = QVBoxLayout(self)
        layout.setSpacing(5)
        # ===== current phase =====
        self.current_phase_lbl.setObjectName('currentPhaseValueLabel')
        layout.addWidget(self.current_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        # ===== minutes before target =====
        # ----- group -----
        group = QWidget()
        group.setObjectName('minutesBeforeTargetGroup')
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- group_layout -----
        group_layout = QHBoxLayout(group)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.setSpacing(5)
        # ----- label -----
        label = QLabel('Minutes Before Target:')
        label.setObjectName('minutesBeforeTargetLabel')
        group_layout.addWidget(label, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- value_label -----
        self.minutes_before_target_lbl.setObjectName('minutesBeforeTargetValueLabel')
        group_layout.addWidget(self.minutes_before_target_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        # ===== next phase =====
        # ----- group -----
        group = QWidget()
        group.setObjectName('nextPhaseGroup')
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- group_layout -----
        group_layout = QHBoxLayout(group)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.setSpacing(5)
        # ----- label -----
        label = QLabel('Next Phase:')
        label.setObjectName('nextPhaseLabel')
        group_layout.addWidget(label, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- value_label -----
        self.next_phase_lbl.setObjectName('nextPhaseValueLabel')
        group_layout.addWidget(self.next_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)

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
