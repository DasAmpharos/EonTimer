import functools
from typing import Final, override

from PySide6.QtGui import Qt
from PySide6.QtWidgets import QFrame, QPushButton, QScrollArea, QSizePolicy, QVBoxLayout, QWidget

from eon_timer.timers.calibrator import Calibrator
from eon_timer.timers.timer_widget import TimerWidget
from eon_timer.util import pyside
from eon_timer.util.injector import component
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.pyside.name_service import NameService
from .custom_phase import CustomPhase
from .custom_phase_widget import CustomPhaseWidget
from .model import CustomTimerModel
from .timer import CustomTimer


@component()
class CustomTimerWidget(TimerWidget[CustomTimerModel, CustomTimer], QWidget):
    def __init__(self,
                 timer: CustomTimer,
                 model: CustomTimerModel,
                 calibrator: Calibrator,
                 name_service: NameService):
        self.calibrator: Final = calibrator
        self.name_service: Final = name_service
        self.__container_layout: Final = QVBoxLayout()

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
        scroll_area.setSizePolicy(
            QSizePolicy.Expanding,
            QSizePolicy.Expanding
        )
        # ----- container -----
        container = QWidget()
        self.name_service.set_name(container, 'customTimerContainer')
        container.setLayout(self.__container_layout)
        self.__container_layout.setContentsMargins(0, 0, 0, 0)
        self.__container_layout.setSpacing(10)
        for index, phase in enumerate(self.model.phases):
            self.__add_widget(phase, index)
        scroll_pane_layout.addWidget(container, stretch=1, alignment=Qt.AlignmentFlag.AlignTop)

        button = QPushButton(chr(0xf055))
        button.setFont('Font Awesome 5 Free')
        self.name_service.set_name(button, 'customTimerAddButton')
        button.clicked.connect(self.__on_add)
        pyside.set_class(button, ['success'])
        layout.addWidget(button, stretch=0, alignment=Qt.AlignmentFlag.AlignBottom)

    def __on_add(self):
        phase = CustomPhase()
        self.model.append(phase)
        index = len(self.model.phases) - 1
        self.__add_widget(phase, index)

    def __on_remove(self, widget: CustomPhaseWidget):
        if not self.resetting:
            self.__container_layout.removeWidget(widget)
            self.model.remove(widget.model)
            self.timer_changed.emit()
            self.__update_indices()
            widget.deleteLater()

    def __on_change(self, widget: CustomPhaseWidget):
        if not self.resetting:
            self.timer_changed.emit()

    def __add_widget(self, phase: CustomPhase, index: int):
        widget = CustomPhaseWidget(index, phase, self.calibrator)
        widget.changed.connect(functools.partial(self.__on_change, widget))
        widget.removed.connect(functools.partial(self.__on_remove, widget))
        self.__container_layout.addWidget(widget, stretch=1, alignment=Qt.AlignmentFlag.AlignTop)

    def __update_indices(self):
        for i in range(self.__container_layout.count()):
            item = self.__container_layout.itemAt(i)
            widget = item.widget()
            widget.index = i

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
