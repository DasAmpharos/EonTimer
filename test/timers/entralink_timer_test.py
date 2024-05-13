import unittest
from unittest.mock import Mock

from eon_timer.timers import EnhancedEntralinkTimer, EntralinkTimer
from eon_timer.timers.delay_timer import DelayTimer


class EntralinkTimerTest(unittest.TestCase):
    def setUp(self):
        self.delay_timer = Mock(spec=DelayTimer)
        self.entralink_timer = EntralinkTimer(self.delay_timer)

    def test_create(self):
        self.delay_timer.create.return_value = [0.0, 0.0]
        phases = self.entralink_timer.create(100, 200, 300.0, 400.0)
        self.delay_timer.create.assert_called_once_with(100, 200, 300.0)
        self.assertEqual([250.0, -400.0], phases)

    def test_calibrate(self):
        self.entralink_timer.calibrate(100, 200)
        self.delay_timer.calibrate.assert_called_once_with(100, 200)


class EnhancedEntralinkTimerTest(unittest.TestCase):
    def setUp(self):
        self.entralink_timer = Mock(spec=EntralinkTimer)
        self.enhanced_entralink_timer = EnhancedEntralinkTimer(self.entralink_timer)

    def test_create(self):
        self.entralink_timer.create.return_value = [1.0, 2.0]
        phases = self.enhanced_entralink_timer.create(1, 2, 3, 4.0, 5.0, 6)
        self.entralink_timer.create.assert_called_once_with(1, 2, 4.0, 5.0)
        self.assertEqual([1.0, 2.0, 3589.5917553924264], phases)

    def test_calibrate(self):
        # target_advances == advances_hit
        result = self.enhanced_entralink_timer.calibrate(1, 1)
        self.assertEqual(0.0, result)
        # target_advances < advances_hit
        result = self.enhanced_entralink_timer.calibrate(1, 2)
        self.assertEqual(-1194.5305851308087, result)
        # target_advances > advances_hit
        result = self.enhanced_entralink_timer.calibrate(1, 0)
        self.assertEqual(1194.5305851308087, result)

        pass


if __name__ == '__main__':
    unittest.main()
