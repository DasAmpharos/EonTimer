from typing import Final

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.util.injector import component


@component()
class Calibrator:
    def __init__(self, timer_settings: TimerSettingsModel) -> None:
        self.timer_settings: Final = timer_settings

    def to_delays(self, milliseconds: float) -> int:
        return round(milliseconds / self.framerate)

    def to_milliseconds(self, delays: int) -> float:
        return self.framerate * delays

    def calibrate_to_delays(self, milliseconds: float) -> int:
        return milliseconds if self.precision_calibration else self.to_delays(milliseconds)

    def calibrate_to_milliseconds(self, delays: int) -> float:
        return delays if self.precision_calibration else self.to_milliseconds(delays)

    def create_calibration(self, delays: int, seconds: float) -> float:
        return self.to_milliseconds(delays - self.to_delays(seconds * 1000))

    @property
    def console(self) -> Console:
        return self.timer_settings.console.get()

    @property
    def precision_calibration(self) -> bool:
        return self.timer_settings.precision_calibration.get()

    @property
    def framerate(self) -> float:
        return self.console.framerate or self.timer_settings.custom_framerate.get()
