from typing import Final

from eon_timer.settings.timer.model import TimerSettingsModel, Console
from eon_timer.util.injector import component


@component()
class Calibrator:
    def __init__(self, timer_settings: TimerSettingsModel) -> None:
        self.timer_settings: Final = timer_settings

    def to_delays(self, milliseconds: int) -> int:
        return round(milliseconds / self.console.framerate)

    def to_milliseconds(self, delays: int) -> int:
        return round(self.console.framerate * delays)

    def calibrate_to_delays(self, milliseconds: int) -> int:
        return milliseconds if self.precision_calibration else self.to_delays(milliseconds)

    def calibrate_to_milliseconds(self, delays: int) -> int:
        return delays if self.precision_calibration else self.to_milliseconds(delays)

    def create_calibration(self, delays: int, seconds: int) -> int:
        return self.to_milliseconds(delays - self.to_delays(seconds * 1000))

    @property
    def console(self) -> Console:
        return self.timer_settings.console.get()

    @property
    def precision_calibration(self) -> bool:
        return self.timer_settings.precision_calibration.get()
