import functools
import time
from typing import Optional, Final, Iterator

from PySide6.QtCore import QObject

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.settings.timer.model import TimerSettingsModel
from eon_timer.util.clock import Clock
from eon_timer.util.injector import component
from eon_timer.util.pyside.thread import DelegatingQThread


@component()
class PhaseRunner(QObject):
    def __init__(self,
                 state: AppState,
                 timer_settings: TimerSettingsModel,
                 action_settings: ActionSettingsModel):
        super().__init__()
        self.state: Final[AppState] = state
        self.timer_settings: Final[TimerSettingsModel] = timer_settings
        self.action_settings: Final[ActionSettingsModel] = action_settings
        self.__thread: Optional[DelegatingQThread] = None

    def start(self):
        if not self.state.running:
            clock = Clock()
            self.state.running = True

            func = functools.partial(self.__run, clock, self.state.phases)
            self.__thread = DelegatingQThread(func, self)
            self.__thread.start()

    def stop(self):
        if self.state.running:
            self.state.running = False

            self.__thread.quit()
            self.__thread.wait()
            self.__thread.deleteLater()
            self.__thread = None

            self.state.reset()

    def __run(self, clock: Clock, phases: list[float]):
        while self.state.running and self.state.current_phase_index < len(phases):
            current_phase = self.state.current_phase
            # build actions to trigger
            actions = []
            for i in range(self.action_settings.count.get()):
                event = self.action_settings.interval.get() * i
                if event < current_phase:
                    actions.append(event)
            # execute the phase
            self.__execute_phase(current_phase, clock, iter(reversed(actions)))
            self.state.current_phase_index += 1
        self.state.running = False
        self.state.reset()

    def __execute_phase(self,
                        phase: float,
                        clock: Clock,
                        actions: Iterator[int]):
        ticks = 0
        elapsed = clock.tick()
        next_action = next(actions, 0)
        period = self.timer_settings.refresh_interval.get()
        while self.state.running and elapsed < phase:
            adjusted_period = period
            remaining_until_action = phase - elapsed - next_action
            if remaining_until_action < adjusted_period:
                adjusted_period = remaining_until_action

            time.sleep(adjusted_period / 1000)

            delta = clock.tick()
            remaining = phase - elapsed - delta
            if remaining <= next_action:
                self.state.trigger_action()
                next_action = next(actions, None)

            ticks += 1
            elapsed += delta
            if ticks % 4 == 0:
                self.state.current_phase_elapsed = elapsed
