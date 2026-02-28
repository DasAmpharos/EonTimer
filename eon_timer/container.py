from PySide6.QtCore import QSettings
from PySide6.QtWidgets import QApplication

from eon_timer.action.actions import Actions
from eon_timer.action.sound_manager import SoundManager
from eon_timer.action.visual_manager import VisualManager
from eon_timer.app_state import AppState
from eon_timer.app_widget import AppWidget
from eon_timer.app_window import AppWindow
from eon_timer.phase_runner import PhaseRunner
from eon_timer.settings.action.model import ActionSettingsModel
from eon_timer.settings.action.widget import ActionSettingsWidget
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.settings.other.update_model import UpdateSettingsModel
from eon_timer.settings.theme.model import ThemeSettingsModel
from eon_timer.settings.theme.widget import ThemeSettingsWidget
from eon_timer.settings.timer.model import TimerSettingsModel
from eon_timer.settings.timer.widget import TimerSettingsWidget
from eon_timer.theme.theme_engine import ThemeEngine
from eon_timer.theme.theme_manager import ThemeManager
from eon_timer.timer_widget import TimerWidget
from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.custom.model import CustomTimerModel
from eon_timer.timers.custom.timer import CustomTimer
from eon_timer.timers.custom.widget import CustomTimerWidget
from eon_timer.timers.delay_timer import DelayTimer
from eon_timer.timers.entralink_timer import EnhancedEntralinkTimer, EntralinkTimer
from eon_timer.timers.frame_timer import FrameTimer, VariableFrameTimer
from eon_timer.timers.gen3.model import Gen3Model
from eon_timer.timers.gen3.timer import Gen3Timer
from eon_timer.timers.gen3.widget import Gen3TimerWidget
from eon_timer.timers.gen4.model import Gen4Model
from eon_timer.timers.gen4.timer import Gen4Timer
from eon_timer.timers.gen4.widget import Gen4TimerWidget
from eon_timer.timers.gen5.model import Gen5Model
from eon_timer.timers.gen5.timer import Gen5Timer
from eon_timer.timers.gen5.widget import Gen5TimerWidget
from eon_timer.timers.second_timer import SecondTimer
from eon_timer.update_manager import UpdateManager
from eon_timer.util.pyside.name_service import NameService


def build(app: QApplication) -> AppWindow:
    settings = QSettings()

    # Settings models
    timer_settings = TimerSettingsModel(settings)
    action_settings = ActionSettingsModel(settings)
    update_settings = UpdateSettingsModel(settings)

    # Theme manager must start before ThemeSettingsModel reads the current theme name
    theme_manager = ThemeManager(app)
    theme_manager._on_start()
    theme_settings = ThemeSettingsModel(settings, theme_manager)

    # Shared utility
    name_service = NameService(theme_settings)

    # Timer computation
    calibrator = Calibrator(timer_settings)
    second_timer = SecondTimer()
    delay_timer = DelayTimer(calibrator, second_timer)
    frame_timer = FrameTimer(calibrator)
    variable_frame_timer = VariableFrameTimer(frame_timer)
    entralink_timer = EntralinkTimer(delay_timer)
    enhanced_entralink_timer = EnhancedEntralinkTimer(entralink_timer)

    # App state and phase runner
    app_state = AppState()
    phase_runner = PhaseRunner(app_state, timer_settings, action_settings)

    # Per-gen settings models
    gen3_model = Gen3Model(settings)
    gen4_model = Gen4Model(settings)
    gen5_model = Gen5Model(settings)
    custom_model = CustomTimerModel(settings)

    # Timer computation classes
    gen3_timer = Gen3Timer(frame_timer, variable_frame_timer)
    gen4_timer = Gen4Timer(calibrator, delay_timer)
    gen5_timer = Gen5Timer(calibrator, delay_timer, second_timer, entralink_timer, enhanced_entralink_timer)
    custom_timer = CustomTimer(calibrator)

    # Timer display widget (top bar showing countdown)
    timer_widget = TimerWidget(app_state, action_settings, name_service)

    # Action handling (audio + visual feedback)
    sound_manager = SoundManager()
    visual_manager = VisualManager(timer_widget, action_settings)
    _actions = Actions(app_state, sound_manager, visual_manager, action_settings)

    # Timer input widgets
    gen3_widget = Gen3TimerWidget(app_state, gen3_model, gen3_timer, name_service)
    gen4_widget = Gen4TimerWidget(gen4_model, gen4_timer, name_service)
    gen5_widget = Gen5TimerWidget(app_state, gen5_model, gen5_timer, name_service)
    custom_widget = CustomTimerWidget(custom_timer, custom_model, calibrator, name_service)

    # Settings widgets
    action_settings_widget = ActionSettingsWidget(name_service, action_settings)
    timer_settings_widget = TimerSettingsWidget(name_service, timer_settings, update_settings)
    theme_settings_widget = ThemeSettingsWidget(theme_settings, theme_manager, name_service)
    settings_dialog = SettingsDialog(
        settings,
        name_service,
        action_settings_widget,
        timer_settings_widget,
        theme_settings_widget,
    )

    update_manager = UpdateManager(update_settings)

    # Main window
    app_widget = AppWidget(
        app_state,
        phase_runner,
        name_service,
        timer_widget,
        gen5_widget,
        gen4_widget,
        gen3_widget,
        custom_widget,
        settings_dialog,
        settings,
    )
    app_window = AppWindow(app_widget, update_manager, name_service, settings)

    # Wire up new signals
    app_widget.status_message.connect(app_window.statusBar().showMessage)
    action_settings_widget.test_action.connect(_actions.trigger)

    # Theme engine wires itself reactively; must be created after app_window and settings_dialog
    _theme_engine = ThemeEngine(app_window, settings_dialog, theme_settings, theme_manager)

    # Register close listeners — called when Qt event loop exits
    app.aboutToQuit.connect(phase_runner._on_close)
    app.aboutToQuit.connect(app_widget._on_close)

    return app_window
