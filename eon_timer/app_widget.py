from typing import Final

from PySide6.QtCore import QSettings, Qt
from PySide6.QtWidgets import *

from eon_timer.app_state import AppState
from eon_timer.phase_runner import PhaseRunner
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.timer_widget import TimerWidget
from eon_timer.timers.custom.widget import CustomTimerWidget
from eon_timer.timers.gen3 import Gen3TimerWidget
from eon_timer.timers.gen4 import Gen4TimerWidget
from eon_timer.timers.gen5 import Gen5TimerWidget
from eon_timer.util import pyside
from eon_timer.util.clock import Clock
from eon_timer.util.injector import component
from eon_timer.util.injector.lifecycle import CloseListener
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.name_service import NameService


@component()
class AppWidget(QWidget, CloseListener):
    TAB_INDEX: Final = 'tab_index'

    def __init__(self,
                 state: AppState,
                 phase_runner: PhaseRunner,
                 name_service: NameService,
                 timer_widget: TimerWidget,
                 gen5_timer_widget: Gen5TimerWidget,
                 gen4_timer_widget: Gen4TimerWidget,
                 gen3_timer_widget: Gen3TimerWidget,
                 custom_timer_widget: CustomTimerWidget,
                 settings_dialog: SettingsDialog,
                 settings: QSettings) -> None:
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
        self.gen5_timer_widget.timer_changed.connect(self.__update_phases)
        self.gen4_timer_widget.timer_changed.connect(self.__update_phases)
        self.gen3_timer_widget.timer_changed.connect(self.__update_phases)
        self.custom_timer_widget.timer_changed.connect(self.__update_phases)
        self.tab_widget.addTab(self.gen5_timer_widget, '5')
        self.tab_widget.addTab(self.gen4_timer_widget, '4')
        self.tab_widget.addTab(self.gen3_timer_widget, '3')
        self.tab_widget.addTab(self.custom_timer_widget, 'C')
        self.name_service.set_name(self.tab_widget, 'timerTabWidget')
        layout.addWidget(self.tab_widget, 0, 1, 2, 3)
        # ----- load settings -----
        current_index = self.settings.value(self.TAB_INDEX, 0, int)
        self.tab_widget.setCurrentIndex(current_index)

        # ----- settings_btn -----
        self.settings_btn.setText(chr(0xf013))
        self.settings_btn.setFont('Font Awesome 5 Free')
        self.settings_btn.clicked.connect(self.__on_settings_btn_clicked)
        self.settings_btn.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        self.name_service.set_name(self.settings_btn, 'settingsButton')
        layout.addWidget(self.settings_btn, 2, 0)
        # ----- reset_btn -----
        button = QPushButton(chr(0xf2ea))
        button.setFont('Font Awesome 5 Free')
        button.clicked.connect(self.__on_reset_btn_clicked)
        button.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        button.setToolTip('Reset Timer')
        self.name_service.set_name(button, 'resetButton')
        layout.addWidget(button, 2, 1)
        # ----- update_btn -----
        self.update_btn.setText('Update')
        self.update_btn.clicked.connect(self.__on_update_btn_clicked)
        self.update_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        self.name_service.set_name(self.update_btn, 'updateButton')
        layout.addWidget(self.update_btn, 2, 2)
        # ----- timer_btn -----
        self.timer_btn.setText('Start')
        self.timer_btn.pressed.connect(self.__on_timer_btn_pressed)
        self.timer_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        self.timer_btn.setFocusPolicy(Qt.FocusPolicy.StrongFocus)
        self.name_service.set_name(self.timer_btn, 'timerButton')
        layout.addWidget(self.timer_btn, 2, 3)
        # ----- running_changed -----
        self.state.running_changed.connect(self.__on_running_changed)

    def __on_timer_btn_pressed(self):
        clock = Clock()
        if not self.state.running:
            self.phase_runner.start(clock)
        else:
            self.phase_runner.stop()

    def __on_update_btn_clicked(self) -> None:
        widget = self.tab_widget.currentWidget()
        calibrate = getattr(widget, 'calibrate', None)
        if calibrate is not None:
            calibrate()

    def __on_settings_btn_clicked(self):
        result = self.settings_dialog.exec()
        if result == QDialog.DialogCode.Accepted:
            self.__update_phases()

    def __on_reset_btn_clicked(self):
        reply = QMessageBox.warning(self,
                                    'Warning',
                                    'Are you sure you want to reset the current timer? This operation cannot be undone.',
                                    QMessageBox.StandardButton.Yes | QMessageBox.StandardButton.No,
                                    QMessageBox.StandardButton.No)
        if reply == QMessageBox.StandardButton.Yes:
            current_widget = self.tab_widget.currentWidget()
            reset = getattr(current_widget, 'reset')
            if reset is not None:
                reset()

    def __on_running_changed(self, running: bool):
        self.timer_btn.setText('Stop' if running else 'Start')
        # disable all tabs except the current tab
        for i in range(self.tab_widget.count()):
            if i != self.tab_widget.currentIndex():
                self.tab_widget.setTabEnabled(i, not running)
        self.settings_btn.setDisabled(running)
        self.update_btn.setDisabled(running)
        current_widget = self.tab_widget.currentWidget()
        current_widget.setDisabled(running)
        # update phases if not running
        if not running:
            self.__update_phases()

    def __update_phases(self):
        widget = self.tab_widget.currentWidget()
        create_phases = getattr(widget, 'create_phases')
        if create_phases is not None:
            phases = create_phases()
            self.state.phases = phases

    def _on_close(self):
        self.settings.setValue(self.TAB_INDEX, self.tab_widget.currentIndex())
