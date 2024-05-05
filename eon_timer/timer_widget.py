from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QLabel, QVBoxLayout, QHBoxLayout, QWidget

from eon_timer.app_state import AppState
from eon_timer.util import const
from eon_timer.util.injector import component
from eon_timer.util.pyside.name_service import NameService


@component()
class TimerWidget(QGroupBox):
    def __init__(self,
                 state: AppState,
                 name_service: NameService):
        super().__init__()
        self.state: Final = state
        self.name_service: Final = name_service
        self.current_phase_lbl: Final = QLabel('0:000')
        self.minutes_before_target_lbl: Final = QLabel('0')
        self.next_phase_lbl: Final = QLabel('0:000')
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.name_service.set_name(self, 'timerWidget')
        # ----- layout -----
        layout = QVBoxLayout(self)
        layout.setSpacing(5)
        # ===== current phase =====
        self.name_service.set_name(self.current_phase_lbl, 'currentPhaseValueLabel')
        layout.addWidget(self.current_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        # ===== minutes before target =====
        # ----- group -----
        group = QWidget()
        self.name_service.set_name(group, 'minutesBeforeTargetGroup')
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- group_layout -----
        group_layout = QHBoxLayout(group)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.setSpacing(5)
        # ----- label -----
        label = QLabel('Minutes Before Target:')
        self.name_service.set_name(label, 'minutesBeforeTargetLabel')
        group_layout.addWidget(label, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- value_label -----
        self.name_service.set_name(self.minutes_before_target_lbl, 'minutesBeforeTargetValueLabel')
        group_layout.addWidget(self.minutes_before_target_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        # ===== next phase =====
        # ----- group -----
        group = QWidget()
        self.name_service.set_name(group, 'nextPhaseGroup')
        layout.addWidget(group, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- group_layout -----
        group_layout = QHBoxLayout(group)
        group_layout.setContentsMargins(0, 0, 0, 0)
        group_layout.setSpacing(5)
        # ----- label -----
        label = QLabel('Next Phase:')
        self.name_service.set_name(label, 'nextPhaseLabel')
        group_layout.addWidget(label, alignment=Qt.AlignmentFlag.AlignLeft)
        # ----- value_label -----
        self.name_service.set_name(self.next_phase_lbl, 'nextPhaseValueLabel')
        group_layout.addWidget(self.next_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)

    def __init_listeners(self):
        self.state.current_phase_changed.connect(self.__on_current_phase_changed)
        self.state.current_phase_elapsed_changed.connect(self.__on_current_phase_changed)
        self.state.minutes_before_target_changed.connect(self.__on_minutes_before_target_changed)
        self.state.next_phase_changed.connect(self.__on_next_phase_changed)

    def __on_current_phase_changed(self):
        current_phase = self.state.current_phase
        current_phase_elapsed = self.state.current_phase_elapsed
        value = current_phase_elapsed if current_phase == const.INFINITY else current_phase - current_phase_elapsed
        self.current_phase_lbl.setText(self.__format_time(value))

    def __on_minutes_before_target_changed(self, new_value: int):
        value = '?' if new_value < 0 else str(new_value)
        self.minutes_before_target_lbl.setText(value)

    def __on_next_phase_changed(self, new_value: float):
        self.next_phase_lbl.setText(self.__format_time(new_value))

    @staticmethod
    def __format_time(milliseconds: int | float) -> str:
        if milliseconds == const.INFINITY:
            return '?:???'
        if isinstance(milliseconds, float):
            milliseconds = int(milliseconds)

        seconds = milliseconds // 1000
        milliseconds_part = milliseconds % 1000
        return f'{seconds}:{milliseconds_part:03d}'
