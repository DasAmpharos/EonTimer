import unittest

from eon_timer.timers import Calibrator, SecondTimer
from eon_timer.timers.delay_timer import DelayTimer
from test.helpers import build_mock_timer_settings


class DelayTimerTest(unittest.TestCase):
    def setUp(self):
        self.delay_timer = DelayTimer(Calibrator(build_mock_timer_settings(fps=1000.0)), SecondTimer())

    def test_create(self):
        phases = self.delay_timer.create(600, 50, 0)
        self.assertEqual([49_600, 600], phases)

    def test_calibrate(self):
        result = self.delay_timer.calibrate(0, 0)
        self.assertEqual(0, result)
        # < CLOSE_THRESHOLD
        result = self.delay_timer.calibrate(0, 1)
        self.assertEqual(0.75, result)
        # == CLOSE_THRESHOLD
        result = self.delay_timer.calibrate(0, DelayTimer.CLOSE_THRESHOLD)
        self.assertEqual(125.25, result)
        # > CLOSE_THRESHOLD
        result = self.delay_timer.calibrate(0, DelayTimer.CLOSE_THRESHOLD + 1)
        self.assertEqual(168, result)


if __name__ == '__main__':
    unittest.main()
