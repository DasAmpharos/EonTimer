import functools
import time
from typing import Final, Iterator, Optional

from PySide6.QtCore import QObject

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.settings.timer.model import TimerSettingsModel
from eon_timer.util.clock import Clock
from eon_timer.util.injector import component
from eon_timer.util.injector.lifecycle import CloseListener
from eon_timer.util.pyside.thread import DelegatingQThread
from eon_timer.util import loggers


@component()
class PhaseRunner(QObject, CloseListener):
    def __init__(self,
                 state: AppState,
                 timer_settings: TimerSettingsModel,
                 action_settings: ActionSettingsModel):
        super().__init__()
        self.logger: Final = loggers.get_logger(self)

        self.state: Final = state
        self.timer_settings: Final = timer_settings
        self.action_settings: Final = action_settings
        self.__thread: Optional[DelegatingQThread] = None
        self.__running = False

    def start(self, clock: Clock):
        if not self.__running:
            self.__running = True
            self.logger.info('Starting phase runner')
            func = functools.partial(self.__run, clock)
            self.__thread = DelegatingQThread(func, self)
            self.__thread.setObjectName('PhaseRunnerThread')
            self.__thread.finished.connect(self.stop)
            self.__thread.start()

    def stop(self):
        if self.__running:
            self.__running = False
            self.logger.info('Stopping phase runner')
            if not self.__thread.isFinished():
                self.__thread.quit()
                self.__thread.wait()
            self.__thread.deleteLater()
            self.__thread = None
            self.state.running = False

    def __run(self, clock: Clock):
        self.state.running = True
        while self.__running:
            self.__execute_current_phase(clock)
            if not self.__running or self.state.current_phase_index + 1 == len(self.state.phases):
                break
            self.state.current_phase_index += 1

    def __execute_current_phase(self, clock: Clock):
        ticks = 0
        elapsed = clock.tick()
        period = self.timer_settings.refresh_interval.get()

        phase = self.state.current_phase
        actions = self.__build_actions(phase, elapsed)
        next_action = next(actions, 0)
        while self.__running:
            if phase != self.state.current_phase:
                # phase has been updated
                phase = self.state.current_phase
                # immediately break if elapsed time is greater than the new phase
                if elapsed >= phase:
                    break
                # rebuild actions
                actions = self.__build_actions(phase, elapsed)
                next_action = next(actions, 0)

            adjusted_period = period
            remaining_until_action = phase - elapsed - next_action
            if 0 < remaining_until_action < adjusted_period:
                adjusted_period = remaining_until_action
            time.sleep(adjusted_period / 1000)

            elapsed += clock.tick()
            remaining = phase - elapsed
            if remaining <= next_action:
                self.state.action_triggered.emit()
                next_action = next(actions, 0)
            if ticks % 4 == 0:
                self.state.current_phase_elapsed = elapsed
            if elapsed >= phase:
                break
            ticks += 1
        self.logger.info('Finished executing phase: expected=%s, actual=%s', phase, elapsed)

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
