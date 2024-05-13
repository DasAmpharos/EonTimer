import unittest

from eon_timer.timers import SecondTimer


class SecondTimerTest(unittest.TestCase):
    def setUp(self):
        self.second_timer = SecondTimer()

    def test_create(self):
        # < minimum length
        phases = self.second_timer.create(1, 0)
        self.assertEqual([61_200], phases)
        # >= minimum length
        phases = self.second_timer.create(50, 0)
        self.assertEqual([50_200], phases)

    def test_calibrate(self):
        # hit == target
        calibration = self.second_timer.calibrate(1, 1)
        self.assertEqual(0, calibration)
        # hit < target
        calibration = self.second_timer.calibrate(1, 0)
        self.assertEqual(500, calibration)
        # hit > target
        calibration = self.second_timer.calibrate(1, 2)
        self.assertEqual(-500, calibration)
