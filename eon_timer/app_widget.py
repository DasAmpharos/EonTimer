from typing import Final

from PySide6.QtCore import QSettings, Qt, Signal
from PySide6.QtGui import QKeySequence, QShortcut
from PySide6.QtWidgets import QDialog, QGridLayout, QMessageBox, QPushButton, QSizePolicy, QTabWidget, QWidget

from eon_timer.app_state import AppState
from eon_timer.phase_runner import PhaseRunner
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.timer_widget import TimerWidget
from eon_timer.timers.custom.widget import CustomTimerWidget
from eon_timer.timers.gen3.widget import Gen3TimerWidget
from eon_timer.timers.gen4.widget import Gen4TimerWidget
from eon_timer.timers.gen5.widget import Gen5TimerWidget
from eon_timer.util import pyside
from eon_timer.util.clock import Clock
from eon_timer.util.lifecycle import CloseListener
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.name_service import NameService

_TAB_LABELS = ['Gen 5', 'Gen 4', 'Gen 3', 'Custom']
_TAB_TOOLTIPS = [
    'Black / White / Black 2 / White 2',
    'Diamond / Pearl / Platinum / HeartGold / SoulSilver',
    'Ruby / Sapphire / Emerald / FireRed / LeafGreen',
    'Define your own phase sequence',
]


class AppWidget(QWidget, CloseListener):
    TAB_INDEX: Final = 'tab_index'
    status_message: Final = Signal(str, int)

    def __init__(
        self,
        state: AppState,
        phase_runner: PhaseRunner,
        name_service: NameService,
        timer_widget: TimerWidget,
        gen5_timer_widget: Gen5TimerWidget,
        gen4_timer_widget: Gen4TimerWidget,
        gen3_timer_widget: Gen3TimerWidget,
        custom_timer_widget: CustomTimerWidget,
        settings_dialog: SettingsDialog,
        settings: QSettings,
    ) -> None:
        super().__init__()
        self.state: Final = state
        self.phase_runner: Final = phase_runner
        self.name_service: Final = name_service
        self.timer_widget: Final = timer_widget
        self.gen5_timer_widget: Final = gen5_timer_widget
        self.gen4_timer_widget: Final = gen4_timer_widget
        self.gen3_timer_widget: Final = gen3_timer_widget
        self.custom_timer_widget: Final = custom_timer_widget
        self.settings_dialog: Final = settings_dialog
        self.settings: Final = settings

        self.tab_widget: Final = QTabWidget()
        self.timer_btn: Final = QPushButton()
        self.update_btn: Final = QPushButton()
        self.settings_btn: Final = QPushButton()
        self.reset_btn: Final = QPushButton()
        self.__init_components()

    @log_method_calls()
    def __init_components(self) -> None:
        self.name_service.set_name(self, 'appWidget')
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setColumnMinimumWidth(0, 215)
        layout.setHorizontalSpacing(10)
        layout.setVerticalSpacing(10)
        # ----- timer_widget -----
        layout.addWidget(self.timer_widget, 0, 0)
        pyside.set_class(self.timer_widget, ['themeable-panel', 'themeable-border'])
        self.timer_widget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        # ----- tab_widget -----
        pyside.set_class(self.tab_widget, ['themeable-panel', 'themeable-border'])
        self.tab_widget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding)
        self.tab_widget.currentChanged.connect(self.__update_phases)
        self.tab_widget.currentChanged.connect(self.__update_update_btn_state)
        self.gen5_timer_widget.timer_changed.connect(self.__update_phases)
        self.gen4_timer_widget.timer_changed.connect(self.__update_phases)
        self.gen3_timer_widget.timer_changed.connect(self.__update_phases)
        self.custom_timer_widget.timer_changed.connect(self.__update_phases)
        self.gen5_timer_widget.timer_changed.connect(self.__update_update_btn_state)
        self.gen4_timer_widget.timer_changed.connect(self.__update_update_btn_state)
        self.gen3_timer_widget.timer_changed.connect(self.__update_update_btn_state)
        timer_widgets = [self.gen5_timer_widget, self.gen4_timer_widget, self.gen3_timer_widget, self.custom_timer_widget]
        for widget, label, tooltip in zip(timer_widgets, _TAB_LABELS, _TAB_TOOLTIPS):
            self.tab_widget.addTab(widget, label)
            idx = self.tab_widget.count() - 1
            self.tab_widget.setTabToolTip(idx, tooltip)
        self.name_service.set_name(self.tab_widget, 'timerTabWidget')
        layout.addWidget(self.tab_widget, 0, 1, 2, 3)
        # ----- load settings -----
        current_index = self.settings.value(self.TAB_INDEX, 0, int)
        self.tab_widget.setCurrentIndex(current_index)

        # ----- settings_btn -----
        self.settings_btn.setText(chr(0xF013))
        self.settings_btn.setFont('Font Awesome 5 Free')
        self.settings_btn.clicked.connect(self.__on_settings_btn_clicked)
        self.settings_btn.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        self.settings_btn.setToolTip('Open Settings (Ctrl+,)')
        self.name_service.set_name(self.settings_btn, 'settingsButton')
        layout.addWidget(self.settings_btn, 2, 0)
        # ----- reset_btn -----
        self.reset_btn.setText(chr(0xF2EA))
        self.reset_btn.setFont('Font Awesome 5 Free')
        self.reset_btn.clicked.connect(self.__on_reset_btn_clicked)
        self.reset_btn.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        self.reset_btn.setToolTip('Reset Timer (F5)')
        self.name_service.set_name(self.reset_btn, 'resetButton')
        layout.addWidget(self.reset_btn, 2, 1)
        # ----- update_btn -----
        self.update_btn.setText('Update')
        self.update_btn.clicked.connect(self.__on_update_btn_clicked)
        self.update_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        self.update_btn.setToolTip('Apply calibration from hit values (F6)')
        self.name_service.set_name(self.update_btn, 'updateButton')
        layout.addWidget(self.update_btn, 2, 2)
        # ----- timer_btn -----
        self.timer_btn.setText('Start')
        self.timer_btn.pressed.connect(self.__on_timer_btn_pressed)
        self.timer_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        self.timer_btn.setFocusPolicy(Qt.FocusPolicy.StrongFocus)
        self.timer_btn.setToolTip('Start / Stop timer (Space)')
        self.name_service.set_name(self.timer_btn, 'timerButton')
        layout.addWidget(self.timer_btn, 2, 3)
        # ----- running_changed -----
        self.state.running_changed.connect(self.__on_running_changed)
        # ----- keyboard shortcuts -----
        self.__init_shortcuts()
        # ----- initial update btn state -----
        self.__update_update_btn_state()

    def __init_shortcuts(self):
        space = QShortcut(QKeySequence(Qt.Key.Key_Space), self)
        space.setContext(Qt.ShortcutContext.ApplicationShortcut)
        space.activated.connect(self.__on_timer_btn_pressed)

        f5 = QShortcut(QKeySequence(Qt.Key.Key_F5), self)
        f5.setContext(Qt.ShortcutContext.ApplicationShortcut)
        f5.activated.connect(self.__on_reset_btn_clicked)

        f6 = QShortcut(QKeySequence(Qt.Key.Key_F6), self)
        f6.setContext(Qt.ShortcutContext.ApplicationShortcut)
        f6.activated.connect(self.__on_update_btn_clicked)

        ctrl_comma = QShortcut(QKeySequence('Ctrl+,'), self)
        ctrl_comma.setContext(Qt.ShortcutContext.ApplicationShortcut)
        ctrl_comma.activated.connect(self.__on_settings_btn_clicked)

    def __on_timer_btn_pressed(self):
        clock = Clock()
        if not self.state.running:
            self.phase_runner.start(clock)
        else:
            self.phase_runner.stop()

    def __on_update_btn_clicked(self) -> None:
        if not self.update_btn.isEnabled():
            return
        widget = self.tab_widget.currentWidget()
        calibrate = getattr(widget, 'calibrate', None)
        if calibrate is not None:
            calibrate()
            self.status_message.emit('Calibration applied.', 4000)
            self.__update_update_btn_state()

    def __on_settings_btn_clicked(self):
        result = self.settings_dialog.exec()
        if result == QDialog.DialogCode.Accepted:
            self.__update_phases()

    def __on_reset_btn_clicked(self):
        if self.state.running:
            return
        current_widget = self.tab_widget.currentWidget()
        # Only ask for confirmation if there is a hit value pending
        has_pending = getattr(current_widget, 'can_calibrate', lambda: False)()
        if has_pending:
            reply = QMessageBox.warning(
                self,
                'Warning',
                'Are you sure you want to reset the current timer? This operation cannot be undone.',
                QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No,
                QMessageBox.StandardButton.No,
            )
            if reply != QMessageBox.StandardButton.Yes:
                return
        reset = getattr(current_widget, 'reset')
        if reset is not None:
            reset()

    def __update_update_btn_state(self):
        widget = self.tab_widget.currentWidget()
        can_calibrate = getattr(widget, 'can_calibrate', None)
        if can_calibrate is not None:
            enabled = can_calibrate()
            self.update_btn.setEnabled(enabled)
            if not enabled:
                self.update_btn.setToolTip('Enter a hit value to enable calibration (F6)')
            else:
                self.update_btn.setToolTip('Apply calibration from hit values (F6)')
        else:
            self.update_btn.setEnabled(True)

    def __on_running_changed(self, running: bool):
        self.timer_btn.setText('Stop' if running else 'Start')
        # disable all tabs except the current tab
        for i in range(self.tab_widget.count()):
            enabled = not running or i == self.tab_widget.currentIndex()
            self.tab_widget.setTabEnabled(i, enabled)
            if not enabled:
                self.tab_widget.setTabToolTip(i, 'Stop the timer to switch modes')
            else:
                self.tab_widget.setTabToolTip(i, _TAB_TOOLTIPS[i])
        self.settings_btn.setDisabled(running)
        self.update_btn.setDisabled(running)
        self.reset_btn.setDisabled(running)
        current_widget = self.tab_widget.currentWidget()
        current_widget.setDisabled(running)
        # update phases if not running
        if not running:
            self.__update_phases()
            self.__update_update_btn_state()
            # Prompt user to enter hit values
            widget = self.tab_widget.currentWidget()
            has_calibrate = getattr(widget, 'can_calibrate', None)
            if has_calibrate is not None:
                self.status_message.emit('Run complete — enter values hit and press Update to calibrate.', 8000)

    def __update_phases(self):
        widget = self.tab_widget.currentWidget()
        create_phases = getattr(widget, 'create_phases')
        if create_phases is not None:
            phases = create_phases()
            self.state.phases = phases

    def _on_close(self):
        self.settings.setValue(self.TAB_INDEX, self.tab_widget.currentIndex())
