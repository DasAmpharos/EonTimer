import functools
import logging
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QFrame, QScrollArea, QSizePolicy, QSpinBox, QVBoxLayout, QWidget

from eon_timer.timers import Calibrator, DelayTimer, EnhancedEntralinkTimer, EntralinkTimer, SecondTimer
from eon_timer.util import const, pyside
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormLayout, FormWidget
from eon_timer.util.pyside.name_service import NameService
from .model import Gen5Mode, Gen5Model


@component()
class Gen5TimerWidget(FormWidget):
    timer_changed: Final = Signal()

    class Field(FormWidget.Field):
        MODE = 'Mode'
        # target field names
        TARGET_DELAY = 'Target Delay'
        TARGET_SECOND = 'Target Second'
        TARGET_ADVANCES = 'Target Advances'
        # calibration field names
        CALIBRATION = 'Calibration'
        ENTRALINK_CALIBRATION = 'Entralink Calibration'
        FRAME_CALIBRATION = 'Frame Calibration'
        # result field names
        DELAY_HIT = 'Delay Hit'
        SECOND_HIT = 'Second Hit'
        ADVANCES_HIT = 'Advances Hit'

    def __init__(self,
                 name_service: NameService,
                 model: Gen5Model,
                 calibrator: Calibrator,
                 delay_timer: DelayTimer,
                 second_timer: SecondTimer,
                 entralink_timer: EntralinkTimer,
                 enhanced_entralink_timer: EnhancedEntralinkTimer) -> None:
        super().__init__(name_service)
        self.model: Final = model
        self.calibrator: Final = calibrator
        self.delay_timer: Final = delay_timer
        self.second_timer: Final = second_timer
        self.entralink_timer: Final = entralink_timer
        self.enhanced_entralink_timer: Final = enhanced_entralink_timer
        self.__init_components()
        self.__init_listeners()

    def __init_components(self) -> None:
        self.name_service.set_name(self, 'gen5TimerWidget')
        # ----- layout -----
        self._layout.set_alignment(Qt.AlignmentFlag.AlignTop)
        self._layout.set_content_margins(10, 10, 10, 10)
        # ----- mode -----
        mode_field = EnumComboBox(Gen5Mode)
        bindings.bind_enum_combobox(mode_field, self.model.mode)
        self.add_field(self.Field.MODE, mode_field, name='gen5Mode')
        # ----- scroll_widget -----
        scroll_pane = QWidget()
        self.name_service.set_name(scroll_pane, 'gen5ScrollPane')
        pyside.set_class(scroll_pane, ['themeable-panel'])
        scroll_pane.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        scroll_pane_layout = QVBoxLayout(scroll_pane)
        scroll_pane_layout.setContentsMargins(0, 0, 0, 0)
        scroll_pane_layout.setSpacing(10)

        scroll_area = QScrollArea()
        self.name_service.set_name(scroll_area, 'gen5ScrollArea')
        scroll_area.setFrameShape(QFrame.Shape.NoFrame)
        pyside.set_class(scroll_area, ['themeable-panel', 'themeable-border'])
        scroll_area.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        scroll_area.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded)
        scroll_area.setWidgetResizable(True)
        scroll_area.setWidget(scroll_pane)
        self._layout.add_row(scroll_area)
        scroll_area.setSizePolicy(
            QSizePolicy.Expanding,
            QSizePolicy.Expanding
        )
        # ----- form -----
        form_widget = QWidget()
        self.name_service.set_name(form_widget, 'gen5FormWidget')
        form_widget.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        scroll_pane_layout.addWidget(form_widget, stretch=0, alignment=Qt.AlignmentFlag.AlignTop)
        form_layout = FormLayout(form_widget)
        form_layout.set_spacing(10)
        # ----- target_delay -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_delay)
        self.add_field(self.Field.TARGET_DELAY, field, layout=form_layout, name='gen5TargetDelay')
        # ----- target_second -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_second)
        self.add_field(self.Field.TARGET_SECOND, field, layout=form_layout, name='gen5TargetSecond')
        # ----- target_advances -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.target_advances)
        self.add_field(self.Field.TARGET_ADVANCES, field, layout=form_layout, name='gen5TargetAdvances')
        # ----- calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.calibration)
        self.add_field(self.Field.CALIBRATION, field, layout=form_layout, name='gen5Calibration')
        # ----- entralink_calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.entralink_calibration)
        self.add_field(self.Field.ENTRALINK_CALIBRATION, field, layout=form_layout, name='gen5EntralinkCalibration')
        # ----- frame_calibration -----
        field = QSpinBox()
        field.setRange(const.INT_MIN, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.frame_calibration)
        self.add_field(self.Field.FRAME_CALIBRATION, field, layout=form_layout, name='gen5FrameCalibration')
        # ----- delay_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.delay_hit)
        self.add_field(self.Field.DELAY_HIT, field, name='gen5DelayHit')
        # ----- second_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.second_hit)
        self.add_field(self.Field.SECOND_HIT, field, name='gen5SecondHit')
        # ----- advances_hit -----
        field = QSpinBox()
        field.setRange(0, const.INT_MAX)
        bindings.bind_spinbox(field, self.model.advances_hit)
        self.add_field(self.Field.ADVANCES_HIT, field, name='gen5AdvancesHit')
        # update field visibility
        self.model.mode.on_change(self.__on_mode_changed)
        event = PropertyChangeEvent(None, self.model.mode.get())
        self.__on_mode_changed(event)

    def __init_listeners(self):
        def field_changed(field: Gen5TimerWidget.Field,
                          event: PropertyChangeEvent) -> None:
            logging.info(f'> INFO: Gen5Widget#{field}: {event.new_value}')
            self.timer_changed.emit()

        # mode
        handler = functools.partial(field_changed, self.Field.MODE)
        self.model.mode.on_change(handler)
        # target_delay
        handler = functools.partial(field_changed, self.Field.TARGET_DELAY)
        self.model.target_delay.on_change(handler)
        # target_second
        handler = functools.partial(field_changed, self.Field.TARGET_SECOND)
        self.model.target_second.on_change(handler)
        # target_advances
        handler = functools.partial(field_changed, self.Field.TARGET_ADVANCES)
        self.model.target_advances.on_change(handler)
        # calibration
        handler = functools.partial(field_changed, self.Field.CALIBRATION)
        self.model.calibration.on_change(handler)
        # entralink_calibration
        handler = functools.partial(field_changed, self.Field.ENTRALINK_CALIBRATION)
        self.model.entralink_calibration.on_change(handler)
        # frame_calibration
        handler = functools.partial(field_changed, self.Field.FRAME_CALIBRATION)
        self.model.frame_calibration.on_change(handler)

    def __on_mode_changed(self, event: PropertyChangeEvent[Gen5Mode]):
        self.set_visible(self.Field.TARGET_DELAY,
                         event.new_value != Gen5Mode.STANDARD)
        self.set_visible(self.Field.TARGET_ADVANCES,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.ENTRALINK_CALIBRATION,
                         event.new_value == Gen5Mode.ENTRALINK or
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.FRAME_CALIBRATION,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)
        self.set_visible(self.Field.DELAY_HIT,
                         event.new_value != Gen5Mode.STANDARD)
        self.set_visible(self.Field.SECOND_HIT,
                         event.new_value != Gen5Mode.C_GEAR)
        self.set_visible(self.Field.ADVANCES_HIT,
                         event.new_value == Gen5Mode.ENTRALINK_PLUS)

    def create_phases(self) -> list[float]:
        calibration = self.calibrator.calibrate_to_milliseconds(self.model.calibration.get())
        entralink_calibration = self.calibrator.calibrate_to_milliseconds(self.model.entralink_calibration.get())
        match self.model.mode.get():
            case Gen5Mode.STANDARD:
                return self.second_timer.create(self.model.target_second.get(), calibration)
            case Gen5Mode.C_GEAR:
                return self.delay_timer.create(self.model.target_delay.get(),
                                               self.model.target_second.get(),
                                               calibration)
            case Gen5Mode.ENTRALINK:
                return self.entralink_timer.create(self.model.target_delay.get(),
                                                   self.model.target_second.get(),
                                                   calibration,
                                                   entralink_calibration)
            case Gen5Mode.ENTRALINK_PLUS:
                return self.enhanced_entralink_timer.create(self.model.target_delay.get(),
                                                            self.model.target_second.get(),
                                                            self.model.target_advances.get(),
                                                            calibration,
                                                            entralink_calibration,
                                                            self.model.frame_calibration.get())

    def calibrate(self):
        if self.__can_calibrate():
            mode = self.model.mode.get()
            match mode:
                case Gen5Mode.STANDARD:
                    self.model.calibration.add(self.calibrator.calibrate_to_delays(self.second_calibration))
                case Gen5Mode.C_GEAR:
                    self.model.calibration.add(self.calibrator.calibrate_to_delays(self.delay_calibration))
                case Gen5Mode.ENTRALINK | Gen5Mode.ENTRALINK_PLUS:
                    self.model.calibration.add(
                        self.calibrator.calibrate_to_delays(self.second_calibration))
                    self.model.entralink_calibration.add(
                        self.calibrator.calibrate_to_delays(self.entralink_calibration))
                    if mode == Gen5Mode.ENTRALINK_PLUS:
                        self.model.frame_calibration.add(self.advances_calibration)

            self.model.delay_hit.set(0)
            self.model.second_hit.set(0)
            self.model.advances_hit.set(0)

    def __can_calibrate(self) -> bool:
        match self.model.mode.get():
            case Gen5Mode.STANDARD:
                return self.model.second_hit.get() > 0
            case Gen5Mode.C_GEAR:
                return self.model.delay_hit.get() > 0
            case Gen5Mode.ENTRALINK:
                return (self.model.delay_hit.get() > 0 and
                        self.model.second_hit.get() > 0)
            case Gen5Mode.ENTRALINK_PLUS:
                return (self.model.delay_hit.get() > 0 and
                        self.model.second_hit.get() > 0 and
                        self.model.advances_hit.get() > 0)

    @property
    def delay_calibration(self) -> float:
        return self.delay_timer.calibrate(self.model.target_delay.get(),
                                          self.model.delay_hit.get())

    @property
    def second_calibration(self) -> float:
        return self.second_timer.calibrate(self.model.target_second.get(),
                                           self.model.second_hit.get())

    @property
    def entralink_calibration(self) -> float:
        return self.entralink_timer.calibrate(self.model.target_delay.get(),
                                              self.model.delay_hit.get() - self.second_calibration)

    @property
    def advances_calibration(self) -> float:
        return self.enhanced_entralink_timer.calibrate(self.model.target_advances.get(),
                                                       self.model.advances_hit.get())
