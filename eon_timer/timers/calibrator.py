from eon_timer.settings.timer.config import TimerConfig


class Calibrator:
    def __init__(self, timer_config: TimerConfig) -> None:
        self.timer_config = timer_config

    def to_delays(self, milliseconds: int) -> int:
        return round(milliseconds / self.timer_config.console.framerate)

    def to_milliseconds(self, delays: int) -> int:
        return round(self.timer_config.console.framerate * delays)

    def calibrate_to_delays(self, milliseconds: int) -> int:
        return milliseconds if self.timer_config.precision_calibration else self.to_delays(milliseconds)

    def calibrate_to_milliseconds(self, delays: int) -> int:
        return delays if self.timer_config.precision_calibration else self.to_milliseconds(delays)

    def create_calibration(self, delays: int, milliseconds: int) -> int:
        return self.to_milliseconds(delays - self.to_delays(milliseconds))
