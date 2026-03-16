import unittest

from eon_timer.timers import Calibrator
from test.helpers import build_mock_timer_settings


class CalibratorTest(unittest.TestCase):
    def setUp(self):
        self.timer_settings = build_mock_timer_settings(fps=1000.0)
        self.calibrator = Calibrator(self.timer_settings)

    def test_to_delays(self):
        # banker's rounding (round half to even)
        self.assertEqual(1, self.calibrator.to_delays(1.25))
        self.assertEqual(2, self.calibrator.to_delays(1.5))  # 0.5 rounds to even (2)
        self.assertEqual(2, self.calibrator.to_delays(1.75))
        self.assertEqual(2, self.calibrator.to_delays(2.5))  # 0.5 rounds to even (2)
        self.assertEqual(4, self.calibrator.to_delays(3.5))  # 0.5 rounds to even (4)
        # "whole frames"
        self.assertEqual(1, self.calibrator.to_delays(1.0))
        self.assertEqual(2, self.calibrator.to_delays(2.0))
        self.assertEqual(3, self.calibrator.to_delays(3.0))

    def test_to_milliseconds(self):
        self.timer_settings.custom_framerate.get.return_value = 1000.0  # 1000 fps → 1 ms/frame
        self.assertEqual(1, self.calibrator.to_milliseconds(1))
        self.assertEqual(2, self.calibrator.to_milliseconds(2))
        self.assertEqual(3, self.calibrator.to_milliseconds(3))

        self.timer_settings.custom_framerate.get.return_value = 1000 / 1.5  # → 1.5 ms/frame
        self.assertEqual(2, self.calibrator.to_milliseconds(1))  # round(1.5) = 2 (banker's)
        self.assertEqual(3, self.calibrator.to_milliseconds(2))  # round(3.0) = 3
        self.assertEqual(4, self.calibrator.to_milliseconds(3))  # round(4.5) = 4 (banker's)

        self.timer_settings.custom_framerate.get.return_value = 500.0  # 500 fps → 2 ms/frame
        self.assertEqual(2, self.calibrator.to_milliseconds(1))
        self.assertEqual(4, self.calibrator.to_milliseconds(2))
        self.assertEqual(6, self.calibrator.to_milliseconds(3))
