import functools
from typing import Final, override

from PySide6.QtCore import Qt
from PySide6.QtWidgets import QFrame, QHBoxLayout, QLabel, QPushButton, QScrollArea, QSizePolicy, QVBoxLayout, QWidget

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import pyside
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.name_service import NameService

from .custom_phase import CustomPhase
from .custom_phase_widget import CustomPhaseWidget
from .model import CustomTimerModel
from .timer import CustomTimer


class CustomTimerWidget(TimerWidget[CustomTimerModel, CustomTimer], QWidget):
    def __init__(self, timer: CustomTimer, model: CustomTimerModel, calibrator: Calibrator, name_service: NameService):
        self.calibrator: Final = calibrator
        self.name_service: Final = name_service
        self.__container_layout: Final = QVBoxLayout()
        self.__total_duration_lbl: QLabel | None = None

        QWidget.__init__(self, None)
        TimerWidget.__init__(self, model, timer)

    @override
    @log_method_calls()
    def _init_components(self):
        self.name_service.set_name(self, 'customTimerWidget')
        # ----- layout -----
        layout = QVBoxLayout(self)
        layout.setContentsMargins(10, 10, 10, 10)
        layout.setSpacing(10)
        # ----- scroll_widget -----
        scroll_pane = QWidget()
        self.name_service.set_name(scroll_pane, 'customTimerScrollPane')
        pyside.set_class(scroll_pane, ['themeable-panel'])
        scroll_pane.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        scroll_pane_layout = QVBoxLayout(scroll_pane)
        scroll_pane_layout.setContentsMargins(10, 10, 10, 10)
        scroll_pane_layout.setSpacing(10)
        # ----- scroll_area -----
        scroll_area = QScrollArea()
        self.name_service.set_name(scroll_area, 'customTimerScrollArea')
        scroll_area.setFrameShape(QFrame.Shape.NoFrame)
        pyside.set_class(scroll_area, ['themeable-panel', 'themeable-border'])
        scroll_area.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        scroll_area.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        scroll_area.setWidgetResizable(True)
        scroll_area.setWidget(scroll_pane)
        layout.addWidget(scroll_area)
        scroll_area.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- container -----
        container = QWidget()
        self.name_service.set_name(container, 'customTimerContainer')
        container.setLayout(self.__container_layout)
        self.__container_layout.setContentsMargins(0, 0, 0, 0)
        self.__container_layout.setSpacing(10)
        for index, phase in enumerate(self.model.phases):
            self.__add_widget(phase, index)
        scroll_pane_layout.addWidget(container, stretch=1, alignment=Qt.AlignmentFlag.AlignTop)

        # ----- bottom row: add button + total duration -----
        bottom_row = QWidget()
        bottom_layout = QHBoxLayout(bottom_row)
        bottom_layout.setContentsMargins(0, 0, 0, 0)
        bottom_layout.setSpacing(10)

        button = QPushButton(chr(0xF055))
        button.setFont('Font Awesome 5 Free')
        self.name_service.set_name(button, 'customTimerAddButton')
        button.clicked.connect(self.__on_add)
        pyside.set_class(button, ['success'])
        bottom_layout.addWidget(button, stretch=0)

        self.__total_duration_lbl = QLabel()
        self.name_service.set_name(self.__total_duration_lbl, 'customTimerTotalDuration')
        self.__total_duration_lbl.setAlignment(Qt.AlignmentFlag.AlignRight | Qt.AlignmentFlag.AlignVCenter)
        bottom_layout.addWidget(self.__total_duration_lbl, stretch=1)

        layout.addWidget(bottom_row, stretch=0)
        self.__update_total_duration()

    def __on_add(self):
        phase = CustomPhase()
        self.model.append(phase)
        index = len(self.model.phases) - 1
        self.__add_widget(phase, index)
        self.__update_removable()
        self.__update_total_duration()

    def __on_remove(self, widget: CustomPhaseWidget):
        if not self.resetting:
            self.__container_layout.removeWidget(widget)
            self.model.remove(widget.model)
            self.timer_changed.emit()
            self.__update_indices()
            widget.deleteLater()
            self.__update_removable()
            self.__update_total_duration()

    def __on_change(self, widget: CustomPhaseWidget):
        if not self.resetting:
            self.timer_changed.emit()
            self.__update_total_duration()

    def __add_widget(self, phase: CustomPhase, index: int):
        widget = CustomPhaseWidget(index, phase, self.calibrator)
        widget.changed.connect(functools.partial(self.__on_change, widget))
        widget.removed.connect(functools.partial(self.__on_remove, widget))
        self.__container_layout.addWidget(widget, stretch=1, alignment=Qt.AlignmentFlag.AlignTop)
        self.__update_removable()

    def __update_indices(self):
        for i in range(self.__container_layout.count()):
            item = self.__container_layout.itemAt(i)
            widget = item.widget()
            widget.index = i

    def __update_removable(self):
        count = self.__container_layout.count()
        for i in range(count):
            item = self.__container_layout.itemAt(i)
            widget = item.widget()
            if isinstance(widget, CustomPhaseWidget):
                widget.set_removable(count > 1)

    def __update_total_duration(self):
        if self.__total_duration_lbl is None:
            return
        total_ms = sum(self.create_phases())
        total_s = total_ms / 1000.0
        if total_s >= 60:
            mins = int(total_s) // 60
            secs = total_s - mins * 60
            self.__total_duration_lbl.setText(f'Total: {mins}m {secs:.1f}s')
        else:
            self.__total_duration_lbl.setText(f'Total: {total_s:.1f}s')

    @override
    def calibrate(self):
        for i in range(self.__container_layout.count()):
            item = self.__container_layout.itemAt(i)
            widget = item.widget()
            widget.calibrate()

    @override
    def _reset(self):
        layout = self.__container_layout
        for i in reversed(range(layout.count())):
            item = layout.itemAt(i)
            widget = item.widget()
            if widget is not None:
                layout.removeWidget(widget)
                widget.setParent(None)
                widget.deleteLater()
