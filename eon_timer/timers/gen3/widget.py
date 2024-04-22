import functools
import logging
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QGroupBox, QPushButton, QSizePolicy, QSpinBox

from eon_timer.timers import FrameTimer
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget, FormLayout
from .model import Gen3Mode, Gen3Model


@component()
class Gen3TimerWidget(FormWidget):
    class Field(FormWidget.Field):
        MODE = 'Mode'
        PRE_TIMER = 'Pre-Timer'
        TARGET_FRAME = 'Target Frame'
        CALIBRATION = 'Calibration'
        SET_TARGET_FRAME = 'Set Target Frame'
        FRAME_HIT = 'Frame Hit'

    timer_changed: Final[Signal] = Signal()

    def __init__(self,
                 model: Gen3Model,
                 frame_timer: FrameTimer) -> None:
        super().__init__(None)
        self.model: Final[Gen3Model] = model
        self.frame_timer: Final[FrameTimer] = frame_timer
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.setObjectName('gen3Widget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        field = EnumComboBox(Gen3Mode)
        bindings.bind_combobox(field, self.model.mode)
        self.model.mode.on_change(self.__on_mode_changed)
        self.add_field(self.Field.MODE, field)
        # ----- form_group -----
        form_group = QGroupBox()
        self._layout.add_row(form_group)
        form_layout = FormLayout(form_group)
        form_layout.set_alignment(Qt.AlignTop)
        pyside.set_class(form_group, ['themeable-panel', 'themeable-border'])
        form_group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        # ----- pre_timer -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.pre_timer)
        self.add_field(self.Field.PRE_TIMER, field, layout=form_layout)
        # ----- target_frame -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_frame)
        self.add_field(self.Field.TARGET_FRAME, field, layout=form_layout)
        # ----- calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibration)
        self.add_field(self.Field.CALIBRATION, field, layout=form_layout)
        # ----- set_target_frame_btn -----
        field = QPushButton(self.Field.SET_TARGET_FRAME.value)
        field_set = self.add_field(self.Field.SET_TARGET_FRAME, field,
                                   layout=form_layout, with_label=False)
        field_set.enabled = False
        # ----- frame_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        self.add_field(self.Field.FRAME_HIT, field)
        # update field visibility
        event = PropertyChangeEvent(None, self.model.mode.get())
        self.__on_mode_changed(event)

    def __init_listeners(self):
        def field_changed(field: Gen3TimerWidget.Field,
                          event: PropertyChangeEvent) -> None:
            logging.info(f'> INFO: Gen3Widget#{field}: {event.new_value}')
            self.timer_changed.emit()

        # mode
        handler = functools.partial(field_changed, self.Field.MODE)
        self.model.mode.on_change(handler)
        # pre_timer
        handler = functools.partial(field_changed, self.Field.PRE_TIMER)
        self.model.pre_timer.on_change(handler)
        # target_frame
        handler = functools.partial(field_changed, self.Field.TARGET_FRAME)
        self.model.target_frame.on_change(handler)
        # calibration
        handler = functools.partial(field_changed, self.Field.CALIBRATION)
        self.model.calibration.on_change(handler)

    def create_phases(self) -> list[int]:
        return self.frame_timer.create(
            self.model.pre_timer.get(),
            self.model.target_frame.get(),
            self.model.calibration.get()
        )

    def calibrate(self):
        if self.model.frame_hit.get() > 0:
            offset = self.frame_timer.calibrate(
                self.model.target_frame.get(),
                self.model.frame_hit.get()
            )
            self.model.calibration.add(offset)
            self.model.frame_hit.set(0)

    def __on_mode_changed(self, event: PropertyChangeEvent[Gen3Mode]) -> None:
        self.set_visible(self.Field.SET_TARGET_FRAME,
                         event.new_value == Gen3Mode.VARIABLE_TARGET)
