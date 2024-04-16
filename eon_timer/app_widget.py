import functools
import importlib.resources
from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtWidgets import *

from eon_timer.app_state import AppState
from eon_timer.phase_runner import PhaseRunner
from eon_timer.settings.dialog import SettingsDialog
from eon_timer.timer_widget import TimerWidget
from eon_timer.timers.gen3 import Gen3Widget
from eon_timer.timers.gen4 import Gen4Widget
from eon_timer.timers.gen5 import Gen5Widget
from eon_timer.util import pyside
from eon_timer.util.injector import component


@component()
class AppWidget(QWidget):
    def __init__(self,
                 state: AppState,
                 gen5_widget: Gen5Widget,
                 gen4_widget: Gen4Widget,
                 gen3_widget: Gen3Widget,
                 timer_widget: TimerWidget,
                 phase_runner: PhaseRunner,
                 settings_dialog: SettingsDialog) -> None:
        super().__init__()
        self.state: Final[AppState] = state
        self.tab_widget: Final[QTabWidget] = QTabWidget()
        self.timer_btn: Final[QPushButton] = QPushButton()
        self.update_btn: Final[QPushButton] = QPushButton()
        self.settings_btn: Final[QPushButton] = QPushButton()
        self.gen5_widget: Final[Gen5Widget] = gen5_widget
        self.gen4_widget: Final[Gen4Widget] = gen4_widget
        self.gen3_widget: Final[Gen3Widget] = gen3_widget
        self.timer_widget: Final[TimerWidget] = timer_widget
        self.phase_runner: Final[PhaseRunner] = phase_runner
        self.settings_dialog: Final[SettingsDialog] = settings_dialog
        self.__init_components()

    def __init_components(self) -> None:
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setColumnMinimumWidth(0, 215)
        layout.setHorizontalSpacing(10)
        layout.setVerticalSpacing(10)
        # ----- timer_widget -----
        layout.addWidget(self.timer_widget, 0, 0)
        self.timer_widget.setObjectName('timerDisplay')
        pyside.set_class(self.timer_widget, ['themeable-panel', 'themeable-border'])
        self.timer_widget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        # ----- tab_widget -----
        layout.addWidget(self.tab_widget, 0, 1, 2, 2)
        pyside.set_class(self.tab_widget, ['themeable-panel', 'themeable-border'])
        self.tab_widget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding)
        self.tab_widget.currentChanged.connect(self.__update_timer)
        self.gen5_widget.timer_changed.connect(self.__update_timer)
        self.gen4_widget.timer_changed.connect(self.__update_timer)
        self.gen3_widget.timer_changed.connect(self.__update_timer)
        self.tab_widget.addTab(self.gen5_widget, '5')
        self.tab_widget.addTab(self.gen4_widget, '4')
        self.tab_widget.addTab(self.gen3_widget, '3')
        # ----- settings_btn -----
        self.settings_btn.setText(chr(0xf013))
        layout.addWidget(self.settings_btn, 2, 0)
        self.settings_btn.clicked.connect(self.__on_settings_btn_clicked)
        self.settings_btn.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed)
        font_data = importlib.resources.read_binary('eon_timer.resources.fonts', 'FontAwesome.ttf')
        self.settings_btn.setFont(pyside.get_font(font_data))
        # ----- update_btn -----
        self.update_btn.setText('Update')
        layout.addWidget(self.update_btn, 2, 1)
        self.update_btn.clicked.connect(self.__on_update_btn_clicked)
        self.update_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        # ----- timer_btn -----
        self.timer_btn.setText('Start')
        layout.addWidget(self.timer_btn, 2, 2)
        self.timer_btn.clicked.connect(self.__on_timer_btn_clicked)
        self.timer_btn.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        self.timer_btn.setFocusPolicy(Qt.FocusPolicy.StrongFocus)
        # ----- running_changed -----
        disable_on_run = [self.tab_widget, self.settings_btn, self.update_btn]
        self.state.running_changed.connect(
            functools.partial(self.__on_running_changed, disable_on_run)
        )

    def __update_timer(self):
        widget = self.tab_widget.currentWidget()
        create_phases = getattr(widget, 'create_phases')
        if create_phases is not None:
            phases = create_phases()
            self.state.phases = phases

    def __on_timer_btn_clicked(self):
        if not self.state.running:
            self.phase_runner.start()
        else:
            self.phase_runner.stop()

    def __on_update_btn_clicked(self) -> None:
        widget = self.tab_widget.currentWidget()
        calibrate = getattr(widget, 'calibrate', None)
        if calibrate is not None:
            calibrate()

    def __on_running_changed(self, disable_on_run: list[QWidget], running: bool):
        self.timer_btn.setText('Stop' if running else 'Start')
        for widget in disable_on_run:
            widget.setEnabled(not running)

    def __on_settings_btn_clicked(self):
        result = self.settings_dialog.exec()
        if result == QDialog.DialogCode.Accepted:
            self.__update_timer()
