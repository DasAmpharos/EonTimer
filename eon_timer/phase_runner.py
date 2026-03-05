import functools
import time
from threading import Event
from typing import Final, Iterator, Optional

from PySide6.QtCore import QObject

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.settings.timer.model import TimerSettingsModel
from eon_timer.util import loggers
from eon_timer.util.clock import Clock
from eon_timer.util.lifecycle import CloseListener
from eon_timer.util.pyside.thread import DelegatingQThread

# Spin-wait for the last N ms of each phase instead of sleeping.
# This ensures action triggers fire precisely even if the OS sleep overshoots.
_SPINWAIT_MS: Final = 2.0


class PhaseRunner(QObject, CloseListener):
    def __init__(self, state: AppState, timer_settings: TimerSettingsModel, action_settings: ActionSettingsModel):
        super().__init__()
        self.logger: Final = loggers.get_logger(self)

        self.state: Final = state
        self.timer_settings: Final = timer_settings
        self.action_settings: Final = action_settings
        self.__thread: Optional[DelegatingQThread] = None
        self.__running: Event = Event()

    def start(self, clock: Clock):
        if not self.__running.is_set():
            self.__running.set()
            self.logger.info('Starting phase runner')
            func = functools.partial(self.__run, clock)
            self.__thread = DelegatingQThread(func, self)
            self.__thread.setObjectName('PhaseRunnerThread')
            self.__thread.finished.connect(self.stop)
            self.__thread.start()

    def stop(self):
        if self.__running.is_set():
            self.__running.clear()
            self.logger.info('Stopping phase runner')
            if not self.__thread.isFinished():
                self.__thread.quit()
                self.__thread.wait()
            self.__thread.deleteLater()
            self.__thread = None
            self.state.running = False

    def __run(self, clock: Clock):
        self.state.running = True
        phases = list(self.state.phases)
        while self.__running.is_set():
            self.__execute_current_phase(clock)
            if not self.__running.is_set() or self.state.current_phase_index + 1 == len(phases):
                break
            self.state.current_phase_index += 1

    def __execute_current_phase(self, clock: Clock):
        period = self.timer_settings.refresh_interval.get()
        phase = self.state.current_phase
        ui_update_interval = 32.0  # ms (~30 fps UI update rate)

        # Record the absolute start of this phase. All elapsed measurements use
        # clock.since_start() minus this offset, avoiding accumulated floating-point
        # error from summing many small deltas.
        phase_start = clock.since_start()

        def get_elapsed() -> float:
            return clock.since_start() - phase_start

        elapsed = get_elapsed()
        last_ui_update = 0.0
        actions = self.__build_actions(phase, elapsed)
        next_action = next(actions, 0)

        while self.__running.is_set():
            if phase != self.state.current_phase:
                # Phase was updated externally (e.g. user adjusted target while running)
                phase = self.state.current_phase
                if elapsed >= phase:
                    break
                actions = self.__build_actions(phase, elapsed)
                next_action = next(actions, 0)

            remaining_until_action = phase - elapsed - next_action
            if remaining_until_action > _SPINWAIT_MS:
                # Sleep for most of the wait, leaving a spin-wait window at the end
                sleep_ms = min(period, remaining_until_action - _SPINWAIT_MS)
                time.sleep(sleep_ms / 1000)

            # After sleep (or immediately if within spin-wait window), sample elapsed
            # with the high-resolution absolute reference — no accumulated error.
            elapsed = get_elapsed()
            remaining = phase - elapsed

            if remaining <= next_action:
                self.state.action_triggered.emit()
                next_action = next(actions, 0)
            if elapsed - last_ui_update >= ui_update_interval:
                self.state.current_phase_elapsed = elapsed
                last_ui_update = elapsed
            if elapsed >= phase:
                break

        # Emit final elapsed so the UI shows the true end time
        self.state.current_phase_elapsed = get_elapsed()
        self.logger.info(f'Finished executing phase: expected={phase}, actual={get_elapsed()}')

    def __build_actions(self, phase: float, elapsed: float) -> Iterator[int]:
        actions = []
        remaining = phase - elapsed
        for i in range(self.action_settings.count.get()):
            action = self.action_settings.interval.get() * i
            if action < remaining:
                actions.append(action)
        return iter(reversed(actions))

    def _on_close(self):
        self.stop()
