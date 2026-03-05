import unittest
from unittest.mock import Mock

from eon_timer.timers import DelayTimer, SecondTimer
from eon_timer.timers.gen4.model import Gen4Model
from eon_timer.timers.gen4.timer import Gen4Timer
from test.helpers import build_calibrator


class Gen4TimerTest(unittest.TestCase):
    def setUp(self):
        self.calibrator = build_calibrator(fps=1000.0)
        self.delay_timer = DelayTimer(self.calibrator, SecondTimer())
        self.timer = Gen4Timer(self.calibrator, self.delay_timer)
        self.model = Mock(spec=Gen4Model)

    # ----- create -----

    def test_create(self):
        self.model.target_delay.get.return_value = 600
        self.model.target_second.get.return_value = 50
        self.model.calibrated_delay.get.return_value = 0
        self.model.calibrated_second.get.return_value = 0

        phases = self.timer.create(self.model)

        self.assertEqual(2, len(phases))
        # phase 2 should be target_delay in ms minus calibration
        self.assertEqual(600.0, phases[1])

    def test_create_with_calibration(self):
        self.model.target_delay.get.return_value = 600
        self.model.target_second.get.return_value = 50
        # calibrated_delay of 500 delays (at framerate 1.0 = 500ms),
        # target_second of 50s = 50000ms -> to_delays(50000) = 50000
        # create_calibration = to_ms(500 - 50000) = -49500
        self.model.calibrated_delay.get.return_value = 500
        self.model.calibrated_second.get.return_value = 14

        phases = self.timer.create(self.model)

        self.assertEqual(2, len(phases))

    # ----- calibrate -----

    def test_calibrate_updates_delay(self):
        self.model.target_delay.get.return_value = 600
        self.model.delay_hit.get.return_value = 550

        self.timer.calibrate(self.model)

        self.model.calibrated_delay.add.assert_called_once()
        self.model.delay_hit.set.assert_called_once_with(None)

    def test_calibrate_skips_when_delay_hit_zero(self):
        self.model.delay_hit.get.return_value = 0

        self.timer.calibrate(self.model)

        self.model.calibrated_delay.add.assert_not_called()
        self.model.delay_hit.set.assert_not_called()

    def test_calibrate_clears_delay_hit(self):
        self.model.target_delay.get.return_value = 600
        self.model.delay_hit.get.return_value = 600

        self.timer.calibrate(self.model)

        self.model.delay_hit.set.assert_called_once_with(None)

    # ----- get_calibration -----

    def test_get_calibration(self):
        self.model.calibrated_delay.get.return_value = 0
        self.model.calibrated_second.get.return_value = 0

        result = self.timer.get_calibration(self.model)

        self.assertEqual(0.0, result)


if __name__ == '__main__':
    unittest.main()
