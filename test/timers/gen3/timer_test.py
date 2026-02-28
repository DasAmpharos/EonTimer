import unittest
from unittest.mock import Mock

from eon_timer.timers import FrameTimer, VariableFrameTimer
from eon_timer.timers.gen3.model import Gen3Mode, Gen3Model
from eon_timer.timers.gen3.timer import Gen3Timer
from eon_timer.util import const
from test.helpers import build_calibrator


class Gen3TimerTest(unittest.TestCase):
    def setUp(self):
        calibrator = build_calibrator(fps=1000.0)
        self.frame_timer = FrameTimer(calibrator)
        self.variable_frame_timer = VariableFrameTimer(self.frame_timer)
        self.timer = Gen3Timer(self.frame_timer, self.variable_frame_timer)
        self.model = Mock(spec=Gen3Model)

    # ----- create -----

    def test_create_standard(self):
        self.model.mode.get.return_value = Gen3Mode.STANDARD
        self.model.pre_timer.get.return_value = 5000
        self.model.target_frame.get.return_value = 1000
        self.model.calibration.get.return_value = 0.0

        phases = self.timer.create(self.model)

        self.assertEqual([5000, 1000.0], phases)

    def test_create_standard_with_calibration(self):
        self.model.mode.get.return_value = Gen3Mode.STANDARD
        self.model.pre_timer.get.return_value = 5000
        self.model.target_frame.get.return_value = 1000
        self.model.calibration.get.return_value = 50.0

        phases = self.timer.create(self.model)

        self.assertEqual([5000, 1050.0], phases)

    def test_create_variable_target(self):
        self.model.mode.get.return_value = Gen3Mode.VARIABLE_TARGET
        self.model.pre_timer.get.return_value = 5000

        phases = self.timer.create(self.model)

        self.assertEqual([5000, const.INFINITY], phases)

    def test_create_unsupported_mode_raises(self):
        self.model.mode.get.return_value = 'invalid_mode'
        with self.assertRaises(ValueError):
            self.timer.create(self.model)

    # ----- calibrate -----

    def test_calibrate_updates_calibration(self):
        self.model.target_frame.get.return_value = 1000
        self.model.frame_hit.get.return_value = 950

        self.timer.calibrate(self.model)

        self.model.calibration.add.assert_called_once_with(50.0)
        self.model.frame_hit.set.assert_called_once_with(None)

    def test_calibrate_exact_hit_adds_zero(self):
        self.model.target_frame.get.return_value = 1000
        self.model.frame_hit.get.return_value = 1000

        self.timer.calibrate(self.model)

        self.model.calibration.add.assert_called_once_with(0.0)
        self.model.frame_hit.set.assert_called_once_with(None)

    def test_calibrate_over_target_subtracts(self):
        self.model.target_frame.get.return_value = 1000
        self.model.frame_hit.get.return_value = 1050

        self.timer.calibrate(self.model)

        self.model.calibration.add.assert_called_once_with(-50.0)
        self.model.frame_hit.set.assert_called_once_with(None)

    def test_calibrate_clears_frame_hit(self):
        self.model.target_frame.get.return_value = 500
        self.model.frame_hit.get.return_value = 500

        self.timer.calibrate(self.model)

        self.model.frame_hit.set.assert_called_once_with(None)


if __name__ == '__main__':
    unittest.main()
