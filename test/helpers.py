from unittest.mock import Mock

from eon_timer.settings.timer.model import Console, TimerSettingsModel
from eon_timer.timers.calibrator import Calibrator
from eon_timer.util.properties.property import BoolProperty, EnumProperty, FloatProperty, IntProperty


def build_mock_timer_settings(fps: float = 1000.0,
                               precision_calibration: bool = False,
                               minimum_length: int = 14) -> TimerSettingsModel:
    """Create a mock TimerSettingsModel for use in unit tests.

    Args:
        fps: Custom framerate in frames-per-second. Defaults to 1000.0 (→ 1 ms/frame for simple arithmetic).
        precision_calibration: Whether precision calibration is enabled.
        minimum_length: Minimum phase length in seconds. Defaults to 14.
    """
    settings = Mock(spec=TimerSettingsModel)
    settings.console = Mock(spec=EnumProperty)
    settings.console.get.return_value = Console.CUSTOM
    settings.custom_framerate = Mock(spec=FloatProperty)
    settings.custom_framerate.get.return_value = fps
    settings.precision_calibration = Mock(spec=BoolProperty)
    settings.precision_calibration.get.return_value = precision_calibration
    settings.minimum_length = Mock(spec=IntProperty)
    settings.minimum_length.get.return_value = minimum_length
    return settings


def build_calibrator(fps: float = 1000.0, precision_calibration: bool = False) -> Calibrator:
    """Create a Calibrator backed by a mock TimerSettingsModel.

    Args:
        fps: Custom framerate in frames-per-second. Defaults to 1000.0 (→ 1 ms/frame for simple arithmetic).
        precision_calibration: Whether precision calibration is enabled.
    """
    return Calibrator(build_mock_timer_settings(fps, precision_calibration))
