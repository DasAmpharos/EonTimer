from typing import Final, override

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.delay_timer import DelayTimer
from eon_timer.timers.entralink_timer import EnhancedEntralinkTimer, EntralinkTimer
from eon_timer.timers.second_timer import SecondTimer
from eon_timer.timers.timer import Timer
from eon_timer.util.injector import component
from .model import Gen5Mode, Gen5Model


@component()
class Gen5Timer(Timer[Gen5Model]):
    def __init__(self,
                 calibrator: Calibrator,
                 delay_timer: DelayTimer,
                 second_timer: SecondTimer,
                 entralink_timer: EntralinkTimer,
                 enhanced_entralink_timer: EnhancedEntralinkTimer):
        self.calibrator: Final = calibrator
        self.delay_timer: Final = delay_timer
        self.second_timer: Final = second_timer
        self.entralink_timer: Final = entralink_timer
        self.enhanced_entralink_timer: Final = enhanced_entralink_timer

    @override
    def create(self, model: Gen5Model) -> list[float]:
        calibration = self.calibrator.calibrate_to_milliseconds(model.calibration.get())
        entralink_calibration = self.calibrator.calibrate_to_milliseconds(model.entralink_calibration.get())

        match model.mode.get():
            case Gen5Mode.STANDARD:
                return self.second_timer.create(model.target_second.get(),
                                                calibration)
            case Gen5Mode.C_GEAR:
                return self.delay_timer.create(model.target_delay.get(),
                                               model.target_second.get(),
                                               calibration)
            case Gen5Mode.ENTRALINK:
                return self.entralink_timer.create(model.target_delay.get(),
                                                   model.target_second.get(),
                                                   calibration,
                                                   entralink_calibration)
            case Gen5Mode.ENTRALINK_PLUS:
                return self.enhanced_entralink_timer.create(model.target_delay.get(),
                                                            model.target_second.get(),
                                                            model.target_advances.get(),
                                                            calibration,
                                                            entralink_calibration,
                                                            model.frame_calibration.get())

    @override
    def calibrate(self, model: Gen5Model):
        delay_calibration = self.get_delay_calibration(model)
        second_calibration = self.get_second_calibration(model)
        entralink_calibration = self.get_entralink_calibration(model)

        match model.mode.get():
            case Gen5Mode.STANDARD:
                model.calibration.add(second_calibration)
            case Gen5Mode.C_GEAR:
                model.calibration.add(delay_calibration)
            case Gen5Mode.ENTRALINK | Gen5Mode.ENTRALINK_PLUS:
                if model.second_hit.get() != model.target_second.get():
                    model.calibration.add(second_calibration)
                if model.delay_hit.get() != model.target_delay.get():
                    model.entralink_calibration.add(entralink_calibration)
                if model.mode.get() == Gen5Mode.ENTRALINK_PLUS:
                    if model.advances_hit.get() != model.target_advances.get():
                        model.frame_calibration.add(self.get_advances_calibration(model))

        model.delay_hit.set(None)
        model.second_hit.set(None)
        model.advances_hit.set(None)

    def get_delay_calibration(self, model: Gen5Model) -> int:
        calibration = self.delay_timer.calibrate(model.target_delay.get(), model.delay_hit.get())
        return self.calibrator.calibrate_to_delays(calibration)

    def get_second_calibration(self, model: Gen5Model) -> int:
        calibration = self.second_timer.calibrate(model.target_second.get(), model.second_hit.get())
        return self.calibrator.calibrate_to_delays(calibration)

    def get_entralink_calibration(self, model: Gen5Model) -> int:
        calibration = self.entralink_timer.calibrate(model.target_delay.get(), model.delay_hit.get())
        return self.calibrator.calibrate_to_delays(calibration)

    def get_advances_calibration(self, model: Gen5Model) -> float:
        return self.enhanced_entralink_timer.calibrate(model.target_advances.get(),
                                                       model.advances_hit.get())
