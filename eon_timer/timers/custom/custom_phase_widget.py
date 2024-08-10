import functools
from typing import Final

from PySide6.QtCore import Qt, Signal
from PySide6.QtWidgets import QGroupBox, QHBoxLayout, QLabel, QPushButton, QSizePolicy, QWidget

from eon_timer.timers import Calibrator
from eon_timer.util import loggers, pyside, strings
from eon_timer.util.const import INT_MAX, INT_MIN
from eon_timer.util.loggers import log_method_calls
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import IntProperty
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside import EnumComboBox
from eon_timer.util.pyside.form import FormWidget
from eon_timer.util.pyside.numeric_input_field import BlankBehavior, FloatInputField, IntInputField, Radix
from .custom_phase import CustomPhase


class CustomPhaseWidget(QWidget):
    changed: Final = Signal()
    removed: Final = Signal()

    class Field(FormWidget.Field):
        UNIT = 'Unit'
        TARGET = 'Target'
        CALIBRATION = 'Calibration'
        HIT = 'Hit'

    def __init__(self,
                 index: int,
                 model: CustomPhase,
                 calibrator: Calibrator):
        super().__init__()
        self.logger: Final = loggers.get_logger(self)
        self.logger.debug('__init__(index=%s)', index)

        self.model: Final = model
        self.calibrator: Final = calibrator
        self.index: Final = IntProperty(index)

        self.__target_field: Final = IntInputField()
        self.__calibration_field: Final = FloatInputField()
        self.__hit_field: Final = IntInputField()
        self.__init_components()

    @log_method_calls()
    def __init_components(self):
        # ----- layout -----
        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)
        layout.setSpacing(10)
        # ----- index -----
        label = QLabel()
        layout.addWidget(label, stretch=0)
        label.setSizePolicy(QSizePolicy.Fixed, QSizePolicy.Fixed)
        update_index = functools.partial(self.__update_index, label)
        self.index.on_change(update_index)
        update_index()
        # ----- group -----
        group = QGroupBox()
        layout.addWidget(group, stretch=1)
        group.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        pyside.set_class(group, ['themeable-panel', 'themeable-border'])
        group_layout = QHBoxLayout(group)
        # ----- form -----
        form = FormWidget()
        group_layout.addWidget(form, stretch=1)
        form.setSizePolicy(QSizePolicy.Expanding, QSizePolicy.Expanding)
        form_layout = form.layout()
        form_layout.setContentsMargins(0, 0, 0, 0)
        form_layout.setSpacing(10)
        # ----- unit -----
        field = EnumComboBox(CustomPhase.Unit)
        bindings.bind_enum_combobox(field, self.model.unit)
        field.value.on_change(self.__on_unit_changed)
        form.add_field(self.Field.UNIT, field)
        self.__on_unit_changed()
        # ----- target -----
        self.__target_field.set_range(0, INT_MAX)
        bindings.bind(self.__target_field.value, self.model.target)
        self.__target_field.value.on_change(self.__on_value_changed)
        form.add_field(self.Field.TARGET, self.__target_field)
        # ----- calibration -----
        self.__calibration_field.set_range(INT_MIN, INT_MAX)
        bindings.bind(self.__calibration_field.value, self.model.calibration)
        self.__calibration_field.value.on_change(self.__on_value_changed)
        form.add_field(self.Field.CALIBRATION, self.__calibration_field)
        # ----- hit_field -----
        self.__hit_field.set_range(0, INT_MAX)
        self.__hit_field.blank_behavior = BlankBehavior.BLANK
        bindings.bind(self.__hit_field.value, self.model.hit)
        form.add_field(self.Field.HIT, self.__hit_field)
        self.__hit_field.setText('')
        # ----- remove_btn -----
        button = QPushButton(chr(0xf057))
        button.setFont('Font Awesome 5 Free')
        button.setToolTip('Remove')
        group_layout.addWidget(button, stretch=0, alignment=Qt.AlignmentFlag.AlignTop | Qt.AlignmentFlag.AlignRight)
        button.clicked.connect(self.__on_remove_button_clicked)
        pyside.set_class(button, ['danger'])

    def calibrate(self):
        if strings.strip_to_none(self.__hit_field.text()) is not None:
            if self.unit != CustomPhase.Unit.MILLISECONDS:
                calibration = self.calibrator.to_milliseconds(self.target - self.hit)
            else:
                calibration = self.target - self.hit
            self.model.calibration.add(calibration)
            self.model.hit.set(None)

    def __update_index(self,
                       label: QLabel,
                       event: PropertyChangeEvent[int] | None = None):
        index = self.index.get()
        if event is not None:
            index = event.new_value
        label.setText(f'{index + 1}.')

    def __on_value_changed(self, event: PropertyChangeEvent[int | float]):
        self.changed.emit()

    def __on_unit_changed(self, event: PropertyChangeEvent[CustomPhase.Unit] | None = None):
        unit = self.unit
        if event is not None:
            unit = event.new_value

        radix = Radix.HEXADECIMAL if unit == CustomPhase.Unit.HEX else Radix.DECIMAL
        self.__target_field.radix = radix
        self.__hit_field.radix = radix

        self.changed.emit()

    def __on_remove_button_clicked(self):
        self.removed.emit()

    def deleteLater(self):
        self.model.dispose()
        self.index.dispose()
        super().deleteLater()

    @property
    def unit(self) -> CustomPhase.Unit:
        return self.model.unit.get()

    @property
    def target(self) -> int:
        return self.model.target.get()

    @property
    def calibration(self) -> float:
        return self.model.calibration.get()

    @property
    def hit(self) -> int:
        return self.model.hit.get()
