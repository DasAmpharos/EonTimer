import unittest
from unittest.mock import Mock

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.timers import Calibrator, SecondTimer
from eon_timer.timers.delay_timer import DelayTimer
from eon_timer.util.properties.property import EnumProperty, FloatProperty


class DelayTimerTest(unittest.TestCase):
    def setUp(self):
        timer_settings = Mock(spec=TimerSettingsModel)
        timer_settings.console = Mock(spec=EnumProperty)
        timer_settings.console.get.return_value = Console.CUSTOM
        timer_settings.custom_framerate = Mock(FloatProperty)
        timer_settings.custom_framerate.get.return_value = 1.0
        self.delay_timer = DelayTimer(
            Calibrator(timer_settings),
            SecondTimer()
        )

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
