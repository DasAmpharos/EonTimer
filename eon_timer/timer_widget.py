from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QGroupBox, QHBoxLayout, QLabel, QProgressBar, QVBoxLayout, QWidget

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.util import const
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.name_service import NameService


class TimerWidget(QGroupBox):
    def __init__(self, state: AppState, action_settings: ActionSettingsModel, name_service: NameService):
        super().__init__()
        self.state: Final = state
        self.action_settings: Final = action_settings
        self.name_service: Final = name_service
        self.current_phase_lbl: Final = QLabel('0.000')
        self.current_phase_lbl.setProperty('monospace', True)
        self.phase_index_lbl: Final = QLabel('Phase 1 of 1')
        self.progress_bar: Final = QProgressBar()
        self.next_phase_lbl: Final = QLabel('0.000')
        self.next_phase_lbl.setProperty('monospace', True)
        self.total_time_lbl: Final = QLabel('0m 0.000s')
        self.__init_components()
        self.__init_listeners()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'timerWidget')
        # ----- layout -----
        layout = QVBoxLayout(self)
        layout.setSpacing(5)
        # ===== current phase =====
        self.name_service.set_name(self.current_phase_lbl, 'currentPhaseValueLabel')
        self.current_phase_lbl.setAlignment(Qt.AlignmentFlag.AlignLeft)
        layout.addWidget(self.current_phase_lbl, alignment=Qt.AlignmentFlag.AlignLeft)
        # ===== progress bar =====
        self.name_service.set_name(self.progress_bar, 'timerProgressBar')
        self.progress_bar.setRange(0, 1000)
        self.progress_bar.setValue(0)
        self.progress_bar.setTextVisible(False)
        self.progress_bar.setFixedHeight(6)
        layout.addWidget(self.progress_bar)
        # ===== phase index =====
        self.name_service.set_name(self.phase_index_lbl, 'phaseIndexLabel')
        layout.addWidget(self.phase_index_lbl)
        # ===== next phase =====
        row = QWidget()
        self.name_service.set_name(row, 'nextPhaseRow')
        row_layout = QHBoxLayout(row)
        row_layout.setContentsMargins(0, 0, 0, 0)
        row_layout.setSpacing(5)
        label = QLabel('Next Phase:')
        self.name_service.set_name(label, 'nextPhaseLabel')
        row_layout.addWidget(label)
        self.name_service.set_name(self.next_phase_lbl, 'nextPhaseValueLabel')
        row_layout.addWidget(self.next_phase_lbl)
        row_layout.addStretch(1)
        layout.addWidget(row)
        # ===== total time =====
        row = QWidget()
        self.name_service.set_name(row, 'totalTimeRow')
        row_layout = QHBoxLayout(row)
        row_layout.setContentsMargins(0, 0, 0, 0)
        row_layout.setSpacing(5)
        label = QLabel('Total Time:')
        self.name_service.set_name(label, 'totalTimeLabel')
        row_layout.addWidget(label)
        self.name_service.set_name(self.total_time_lbl, 'totalTimeValueLabel')
        row_layout.addWidget(self.total_time_lbl)
        row_layout.addStretch(1)
        layout.addWidget(row)

    def __init_listeners(self):
        self.state.current_phase_changed.connect(self.__on_current_phase_changed)
        self.state.current_phase_elapsed_changed.connect(self.__on_current_phase_changed)
        self.state.next_phase_changed.connect(self.__on_next_phase_changed)
        self.state.phases_changed.connect(self.__on_phases_changed)
        self.state.running_changed.connect(self.__on_running_changed)

    def __on_current_phase_changed(self):
        current_phase = self.state.current_phase
        current_phase_elapsed = self.state.current_phase_elapsed
        value = current_phase_elapsed if current_phase == const.INFINITY else current_phase - current_phase_elapsed
        self.current_phase_lbl.setText(self.__format_time(value))
        self.__update_progress(current_phase, current_phase_elapsed)
        # Update phase index label
        index = self.state.current_phase_index + 1
        total = len(self.state.phases)
        self.phase_index_lbl.setText(f'Phase {index} of {total}')

    def __update_progress(self, current_phase: float, elapsed: float):
        if current_phase == const.INFINITY or current_phase <= 0:
            self.progress_bar.setValue(0)
            self.progress_bar.setProperty('zone', 'normal')
            self.progress_bar.style().polish(self.progress_bar)
            return

        progress = min(int((elapsed / current_phase) * 1000), 1000)
        self.progress_bar.setValue(progress)

        # Color the bar based on proximity to action zone
        action_interval = self.action_settings.interval.get()
        action_count = self.action_settings.count.get()
        action_zone_start = action_interval * max(action_count - 1, 0) if action_count > 0 else 0
        remaining = current_phase - elapsed
        if remaining <= 0:
            zone = 'complete'
        elif action_zone_start > 0 and remaining <= action_zone_start:
            zone = 'action'
        else:
            zone = 'normal'

        current_zone = self.progress_bar.property('zone')
        if current_zone != zone:
            self.progress_bar.setProperty('zone', zone)
            self.progress_bar.style().polish(self.progress_bar)

    def __on_phases_changed(self):
        total = len(self.state.phases)
        self.phase_index_lbl.setText(f'Phase 1 of {total}')
        self.total_time_lbl.setText(self.__format_total(self.state.phases))

    def __on_running_changed(self, running: bool):
        if not running:
            self.progress_bar.setValue(0)
            self.progress_bar.setProperty('zone', 'normal')
            self.progress_bar.style().polish(self.progress_bar)

    def __on_next_phase_changed(self, new_value: float):
        is_last = self.state.current_phase_index + 1 >= len(self.state.phases)
        self.next_phase_lbl.setText('—' if is_last else f'{self.__format_time(new_value)}s')

    @staticmethod
    def __format_total(phases: list[float]) -> str:
        if const.INFINITY in phases:
            return '∞'
        total_s = sum(phases) / 1000.0
        mins = int(total_s) // 60
        secs = total_s - mins * 60
        return f'{mins}m {secs:.3f}s'

    @staticmethod
    def __format_time(milliseconds: int | float) -> str:
        if milliseconds == const.INFINITY:
            return '∞'
        if isinstance(milliseconds, float):
            milliseconds = int(milliseconds)

        seconds = milliseconds // 1000
        milliseconds_part = milliseconds % 1000
        if seconds >= 1000:
            return f'{seconds}...'
        return f'{seconds}.{milliseconds_part:03d}'
