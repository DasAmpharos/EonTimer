import math
from typing import Final

from eon_timer.settings.timer.model import Console, TimerSettingsModel


class Calibrator:
    def __init__(self, timer_settings: TimerSettingsModel) -> None:
        self.timer_settings: Final = timer_settings

    def to_delays(self, milliseconds: float) -> int:
        return math.floor(milliseconds / self.framerate)

    def to_milliseconds(self, delays: int) -> float:
        return self.framerate * delays

    def calibrate_to_delays(self, milliseconds: float) -> int:
        return round(milliseconds) if self.precision_calibration else self.to_delays(milliseconds)

    def calibrate_to_milliseconds(self, delays: int) -> float:
        return float(delays) if self.precision_calibration else self.to_milliseconds(delays)

    def create_calibration(self, delays: int, seconds: float) -> float:
        return self.to_milliseconds(delays - self.to_delays(seconds * 1000))

    @property
    def console(self) -> Console:
        return self.timer_settings.console.get()

    @property
    def precision_calibration(self) -> bool:
        return self.timer_settings.precision_calibration.get()

    @property
    def minimum_length(self) -> int:
        return self.timer_settings.minimum_length.get() * 1000

    @property
    def framerate(self) -> float:
        custom_framerate = self.timer_settings.custom_framerate.get()
        if custom_framerate == 0:
            raise ValueError('Custom framerate must be greater than 0')
        return self.console.framerate or (1000 / custom_framerate)
