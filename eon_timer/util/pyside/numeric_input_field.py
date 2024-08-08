from abc import abstractmethod
from enum import IntEnum, auto
from numbers import Number
from typing import Callable, Final, Generic, TypeVar, override

from PySide6.QtCore import QObject
from PySide6.QtGui import QDoubleValidator, QKeyEvent, QValidator, Qt
from PySide6.QtWidgets import QLineEdit

from eon_timer.util.properties.property import FloatProperty, IntProperty, Property
from eon_timer.util.properties.property_change import PropertyChangeEvent

NumericT = TypeVar('NumericT', bound=Number)


class BlankBehavior(IntEnum):
    BLANK = auto()
    PLACEHOLDER = auto()


class NumericInputField(QLineEdit, Generic[NumericT]):
    def __init__(self, p_property: Property[NumericT], parent: QObject | None = None):
        QLineEdit.__init__(self, parent)
        self.value: Final = p_property
        self.setText(str(self.get_value()))
        self.value.on_change(self.__on_property_changed)
        self.textChanged.connect(self.__on_text_changed)
        self.blank_behavior = BlankBehavior.PLACEHOLDER

    @abstractmethod
    def _to_str(self, value: NumericT | None) -> str:
        ...

    @abstractmethod
    def _to_value(self, text: str | None) -> NumericT | None:
        ...

    @abstractmethod
    def set_range(self, bottom: NumericT, top: NumericT) -> None:
        ...

    def get_value(self) -> NumericT:
        return self.value.get()

    def set_value(self, new_value: NumericT) -> None:
        self.value.set(new_value)

    def __on_property_changed(self, event: PropertyChangeEvent[NumericT]) -> None:
        self.setText(self._to_str(event.new_value))

    def __on_text_changed(self, text: str) -> None:
        value = self._to_value(text)
        if value is None and self.blank_behavior == BlankBehavior.PLACEHOLDER:
            self.setPlaceholderText(str(self.value.get()))
            return
        if value is not None and value != self.get_value():
            self.setPlaceholderText('')
            self.value.set(value)

    @override
    def keyPressEvent(self, event: QKeyEvent):
        super().keyPressEvent(event)
        match event.key():
            case Qt.Key.Key_Up:
                self.value.set(self.value.get() + 1)
            case Qt.Key.Key_Down:
                self.value.set(self.value.get() - 1)


class Radix(IntEnum):
    BINARY = 2
    OCTAL = 8
    DECIMAL = 10
    HEXADECIMAL = 16


class IntInputField(NumericInputField[int]):
    def __init__(self, value: int = 0, parent: QObject | None = None):
        NumericInputField.__init__(self, IntProperty(value), parent)
        self.validator: Final = RadixValidator(self.to_int)
        super().setValidator(self.validator)

    @staticmethod
    def to_int(text: str, radix: Radix) -> int:
        return int(text, radix)

    @override
    def set_range(self, lower_bound: int, upper_bound: int) -> None:
        self.validator.set_range(lower_bound, upper_bound)
        value = min(max(self.value.get(), lower_bound), upper_bound)
        self.value.set(value)

    @override
    def set_value(self, new_value: int) -> None:
        if self.validator.lower_bound > new_value:
            raise ValueError(f'new_value must be greater than or equal to {self.validator.lower_bound}')
        if self.validator.upper_bound < new_value:
            raise ValueError(f'new_value must be less than or equal to {self.validator.upper_bound}')
        super().set_value(new_value)

    @override
    def _to_str(self, value: int | None) -> str:
        if value is None:
            return ''

        match self.validator.radix:
            case Radix.BINARY:
                return '{:b}'.format(value)
            case Radix.OCTAL:
                return '{:o}'.format(value)
            case Radix.DECIMAL:
                return str(value)
            case Radix.HEXADECIMAL:
                return '{:X}'.format(value)

    @override
    def _to_value(self, text: str | None) -> int | None:
        return None if not text else self.to_int(text, self.validator.radix)

    def set_radix(self, radix: Radix) -> None:
        self.validator.radix = radix
        new_text = self._to_str(self.value.get())
        self.setText(new_text)


class FloatInputField(NumericInputField[float]):
    def __init__(self, value: float = 0.0, parent: QObject | None = None):
        NumericInputField.__init__(self, FloatProperty(value), parent)
        self.__validator = QDoubleValidator()
        super().setValidator(self.__validator)

    @override
    def set_range(self, bottom: float, top: float) -> None:
        self.__validator.setRange(bottom, top)
        value = min(max(self.value.get(), bottom), top)
        self.value.set(value)

    @override
    def set_value(self, new_value: float) -> None:
        if self.__validator.bottom() > new_value:
            raise ValueError(f'new_value must be greater than or equal to {self.__validator.bottom()}')
        if self.__validator.top() < new_value:
            raise ValueError(f'new_value must be less than or equal to {self.__validator.top()}')
        super().set_value(new_value)

    @override
    def _to_str(self, value: float | None) -> str:
        return '' if value is None else '{:.2f}'.format(value)

    @override
    def _to_value(self, text: str | None) -> float | None:
        return None if not text else float(text)


class RadixValidator(QValidator, Generic[NumericT]):
    def __init__(self,
                 converter: Callable[[str, Radix], NumericT],
                 radix: Radix = Radix.DECIMAL,
                 parent: QObject | None = None):
        super().__init__(parent)
        self.lower_bound: NumericT | None = None
        self.upper_bound: NumericT | None = None
        self.converter: Final = converter
        self.radix = radix

    @override
    def validate(self, s: str, pos: int):
        try:
            value = self.converter(s, self.radix)
            if self.lower_bound is not None and value < self.lower_bound:
                return QValidator.State.Invalid, s, pos
            if self.upper_bound is not None and value > self.upper_bound:
                return QValidator.State.Invalid, s, pos
            return QValidator.State.Acceptable, s, pos
        except ValueError:
            if s == '' or s == '-':
                return QValidator.State.Intermediate, s, pos
            return QValidator.State.Invalid, s, pos

    @override
    def fixup(self, s: str) -> str:
        try:
            return str(self.converter(s, self.radix))
        except ValueError:
            return ''

    def set_range(self, lower_bound: NumericT, upper_bound: NumericT):
        if lower_bound > upper_bound:
            raise ValueError(f'lower_bound must be less than or equal to upper_bound')
        self.lower_bound = lower_bound
        self.upper_bound = upper_bound
