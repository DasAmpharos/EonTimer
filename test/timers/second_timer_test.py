import unittest
from unittest.mock import Mock

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.timers import Calibrator, SecondTimer
from eon_timer.util.properties.property import EnumProperty, FloatProperty, IntProperty


class SecondTimerTest(unittest.TestCase):
    def setUp(self):
        timer_settings = Mock(spec=TimerSettingsModel)
        timer_settings.console = Mock(spec=EnumProperty)
        timer_settings.console.get.return_value = Console.CUSTOM
        timer_settings.custom_framerate = Mock(spec=FloatProperty)
        timer_settings.custom_framerate.get.return_value = 1.0
        timer_settings.precision_calibration = Mock(spec=IntProperty)
        timer_settings.precision_calibration.get.return_value = False
        timer_settings.minimum_length = Mock(spec=IntProperty)
        timer_settings.minimum_length.get.return_value = 14
        self.second_timer = SecondTimer(Calibrator(timer_settings))

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
