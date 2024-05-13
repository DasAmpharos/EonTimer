import unittest
from unittest.mock import Mock

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.timers import Calibrator, FrameTimer, VariableFrameTimer
from eon_timer.util import const
from eon_timer.util.properties.property import EnumProperty, FloatProperty


class FrameTimerTest(unittest.TestCase):
    def setUp(self):
        timer_settings = Mock(spec=TimerSettingsModel)
        timer_settings.console = Mock(spec=EnumProperty)
        timer_settings.console.get.return_value = Console.CUSTOM
        timer_settings.custom_framerate = Mock(FloatProperty)
        timer_settings.custom_framerate.get.return_value = 1.0
        self.frame_timer = FrameTimer(
            Calibrator(timer_settings),
        )

    def test_create(self):
        phases = self.frame_timer.create(5000, 1000, 0)
        self.assertEqual([5000, 1000], phases)

    def test_calibrate(self):
        # equals
        result = self.frame_timer.calibrate(0, 0)
        self.assertEqual(0, result)
        # frame_hit < target_frame
        result = self.frame_timer.calibrate(1000, 950)
        self.assertEqual(50, result)
        # frame_hit > target_frame
        result = self.frame_timer.calibrate(1000, 1050)
        self.assertEqual(-50, result)


class VariableFrameTimerTest(unittest.TestCase):
    def setUp(self):
        self.frame_timer = Mock(spec=FrameTimer)
        self.variable_frame_timer = VariableFrameTimer(self.frame_timer)

    def test_create(self):
        phases = self.variable_frame_timer.create(5000)
        self.assertEqual([5000, const.INFINITY], phases)

    def test_calibrate(self):
        # equals
        self.variable_frame_timer.calibrate(0, 0)
        self.frame_timer.calibrate.assert_called_with(0, 0)
        # frame_hit < target_frame
        self.variable_frame_timer.calibrate(1000, 950)
        self.frame_timer.calibrate.assert_called_with(1000, 950)
        # frame_hit > target_frame
        self.variable_frame_timer.calibrate(1000, 1050)
        self.frame_timer.calibrate.assert_called_with(1000, 1050)


if __name__ == '__main__':
    unittest.main()
