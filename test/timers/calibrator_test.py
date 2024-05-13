import unittest
from unittest.mock import Mock

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.timers import Calibrator
from eon_timer.util.properties.property import EnumProperty, FloatProperty


class CalibratorTest(unittest.TestCase):
    def setUp(self):
        self.timer_settings = Mock(spec=TimerSettingsModel)
        self.timer_settings.console = Mock(spec=EnumProperty)
        self.timer_settings.console.get.return_value = Console.CUSTOM
        self.timer_settings.custom_framerate = Mock(FloatProperty)
        self.timer_settings.custom_framerate.get.return_value = 1.0
        self.calibrator = Calibrator(self.timer_settings)

    def test_to_delays(self):
        # "partial" frames
        self.assertEqual(1, self.calibrator.to_delays(1.25))
        self.assertEqual(1, self.calibrator.to_delays(1.5))
        self.assertEqual(1, self.calibrator.to_delays(1.75))
        # "whole frames"
        self.assertEqual(1, self.calibrator.to_delays(1.0))
        self.assertEqual(2, self.calibrator.to_delays(2.0))
        self.assertEqual(3, self.calibrator.to_delays(3.0))

    def test_to_milliseconds(self):
        self.timer_settings.custom_framerate.get.return_value = 1.0
        self.assertEqual(1.0, self.calibrator.to_milliseconds(1))
        self.assertEqual(2.0, self.calibrator.to_milliseconds(2))
        self.assertEqual(3.0, self.calibrator.to_milliseconds(3))

        self.timer_settings.custom_framerate.get.return_value = 1.5
        self.assertEqual(1.5, self.calibrator.to_milliseconds(1))
        self.assertEqual(3, self.calibrator.to_milliseconds(2))
        self.assertEqual(4.5, self.calibrator.to_milliseconds(3))

        self.timer_settings.custom_framerate.get.return_value = 2.0
        self.assertEqual(2.0, self.calibrator.to_milliseconds(1))
        self.assertEqual(4.0, self.calibrator.to_milliseconds(2))
        self.assertEqual(6.0, self.calibrator.to_milliseconds(3))
