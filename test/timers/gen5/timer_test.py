import random
import unittest
from unittest.mock import Mock

from eon_timer.timers import Calibrator, DelayTimer, EnhancedEntralinkTimer, EntralinkTimer, SecondTimer
from eon_timer.timers.gen5.model import Gen5Model, Gen5Mode
from eon_timer.timers.gen5.timer import Gen5Timer


class Gen5TimerTest(unittest.TestCase):
    def setUp(self):
        self.model = Mock(spec=Gen5Model)
        self.calibrator = Mock(spec=Calibrator)
        self.delay_timer = Mock(spec=DelayTimer)
        self.second_timer = Mock(spec=SecondTimer)
        self.entralink_timer = Mock(spec=EntralinkTimer)
        self.enhanced_entralink_timer = Mock(spec=EnhancedEntralinkTimer)
        self.test_subject = Gen5Timer(self.calibrator,
                                      self.delay_timer,
                                      self.second_timer,
                                      self.entralink_timer,
                                      self.enhanced_entralink_timer)

    def test_calibrate_standard_mode(self):
        self.model.mode.get.return_value = Gen5Mode.STANDARD
        self.mock_seconds()

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_called_once()
        self.assert_fields_cleared()

    def test_calibrate_c_gear_mode(self):
        self.model.mode.get.return_value = Gen5Mode.C_GEAR
        self.mock_delays()

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_called_once()
        self.assert_fields_cleared()

    def test_calibrate_entralink_mode_second_calibration(self):
        self.model.mode.get.return_value = Gen5Mode.ENTRALINK
        self.mock_delays(True)
        self.mock_seconds(False)

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_called_once()
        self.model.entralink_calibration.add.assert_not_called()
        self.assert_fields_cleared()

    def test_calibrate_entralink_mode_delay_calibration(self):
        self.model.mode.get.return_value = Gen5Mode.ENTRALINK
        self.mock_delays(False)
        self.mock_seconds(True)

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_not_called()
        self.model.entralink_calibration.add.assert_called_once()
        self.assert_fields_cleared()

    def test_calibrate_entralink_plus_mode(self):
        self.model.mode.get.return_value = Gen5Mode.ENTRALINK_PLUS
        self.mock_delays(True)
        self.mock_seconds(True)
        self.mock_advances(False)

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_not_called()
        self.model.entralink_calibration.add.assert_not_called()
        self.model.frame_calibration.add.assert_called_once()
        self.assert_fields_cleared()

    def test_calibrate_entralink_plus_mode_targets_hit(self):
        self.model.mode.get.return_value = Gen5Mode.ENTRALINK_PLUS
        self.mock_delays(True)
        self.mock_seconds(True)
        self.mock_advances(True)

        self.test_subject.calibrate(self.model)

        self.model.calibration.add.assert_not_called()
        self.model.entralink_calibration.add.assert_not_called()
        self.model.frame_calibration.add.assert_not_called()
        self.assert_fields_cleared()

    def test_get_delay_calibration(self):
        calibration = random.randint(-100, 100)
        target_delay, delay_hit = self.mock_delays(False)
        self.delay_timer.calibrate.return_value = calibration

        self.test_subject.get_delay_calibration(self.model)

        self.delay_timer.calibrate.assert_called_once_with(target_delay, delay_hit)
        self.calibrator.calibrate_to_delays.assert_called_once_with(calibration)

    def test_get_second_calibration(self):
        calibration = random.randint(-100, 100)
        target_second, second_hit = self.mock_seconds(False)
        self.second_timer.calibrate.return_value = calibration

        self.test_subject.get_second_calibration(self.model)

        self.second_timer.calibrate.assert_called_once_with(target_second, second_hit)
        self.calibrator.calibrate_to_delays.assert_called_once_with(calibration)

    def test_get_entralink_calibration(self):
        calibration = random.randint(-100, 100)
        target_delay, delay_hit = self.mock_delays(False)
        self.entralink_timer.calibrate.return_value = calibration

        self.test_subject.get_entralink_calibration(self.model)

        self.entralink_timer.calibrate.assert_called_once_with(target_delay, delay_hit)
        self.calibrator.calibrate_to_delays.assert_called_once_with(calibration)

    def test_get_advances_calibration(self):
        target_advances, advances_hit = self.mock_advances(False)

        self.test_subject.get_advances_calibration(self.model)

        self.enhanced_entralink_timer.calibrate.assert_called_once_with(target_advances, advances_hit)
        self.calibrator.calibrate_to_delays.assert_not_called()

    def mock_delays(self, should_match: bool = True) -> tuple[int, int]:
        target = random.randint(1, 100)
        self.model.target_delay.get.return_value = target
        offset = 0 if should_match else random.randint(1, 10)
        self.model.delay_hit.get.return_value = target + offset
        return target, target + offset

    def mock_seconds(self, should_match: bool = True) -> tuple[int, int]:
        target = random.randint(1, 100)
        self.model.target_second.get.return_value = target
        offset = 0 if should_match else random.randint(1, 10)
        self.model.second_hit.get.return_value = target + offset
        return target, target + offset

    def mock_advances(self, should_match: bool = True) -> tuple[int, int]:
        target = random.randint(1, 100)
        self.model.target_advances.get.return_value = target
        offset = 0 if should_match else random.randint(1, 10)
        self.model.advances_hit.get.return_value = target + offset
        return target, target + offset

    def assert_fields_cleared(self):
        self.model.delay_hit.set.assert_called_once_with(0)
        self.model.second_hit.set.assert_called_once_with(0)
        self.model.advances_hit.set.assert_called_once_with(0)
