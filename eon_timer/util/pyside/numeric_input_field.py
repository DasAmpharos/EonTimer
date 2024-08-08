from abc import abstractmethod
from enum import IntEnum, auto
from numbers import Number
from typing import Callable, Final, Generic, TypeVar, final, override

from PySide6.QtCore import QObject
from PySide6.QtGui import QDoubleValidator, QKeyEvent, QValidator, Qt
from PySide6.QtWidgets import QLineEdit

from eon_timer.util import strings
from eon_timer.util.properties.property import FloatProperty, IntProperty, Property
from eon_timer.util.properties.property_change import PropertyChangeEvent

NumericT = TypeVar('NumericT', bound=Number)


class Radix(IntEnum):
    BINARY = 2
    OCTAL = 8
    DECIMAL = 10
    HEXADECIMAL = 16


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
    def to_number(self, s: str | None) -> NumericT | None:
        ...

    @abstractmethod
    def to_string(self, n: NumericT | None) -> str:
        ...

    @abstractmethod
    def set_range(self, lower_bound: NumericT, upper_bound: NumericT) -> None:
        ...

    @final
    @override
    def keyPressEvent(self, event: QKeyEvent):
        super().keyPressEvent(event)
        match event.key():
            case Qt.Key.Key_Up:
                self.value.set(self.value.get() + 1)
            case Qt.Key.Key_Down:
                self.value.set(self.value.get() - 1)

    def get_value(self) -> NumericT:
        return self.value.get()

    def set_value(self, value: NumericT) -> None:
        self.value.set(value)

    def __on_property_changed(self, event: PropertyChangeEvent[NumericT]) -> None:
        self.setText(self.to_string(event.new_value))

    def __on_text_changed(self, text: str) -> None:
        value = self.to_number(text)
        if value is None and self.blank_behavior == BlankBehavior.PLACEHOLDER:
            self.setPlaceholderText(str(self.value.get()))
            return
        if value is not None and value != self.get_value():
            self.setPlaceholderText('')
            self.value.set(value)


class IntInputField(NumericInputField[int]):
    def __init__(self, value: int = 0, parent: QObject | None = None):
        NumericInputField.__init__(self, IntProperty(value), parent)
        self.validator: Final = RadixValidator(lambda s, radix: int(s, radix))
        super().setValidator(self.validator)

    @override
    def to_number(self, s: str | None) -> int | None:
        s = strings.strip_to_none(s)
        if s is None:
            return None
        return int(s, self.validator.radix)

    @override
    def to_string(self, n: int | None) -> str:
        if n is None:
            return ''

        match self.validator.radix:
            case Radix.BINARY:
                return '{:b}'.format(n)
            case Radix.OCTAL:
                return '{:o}'.format(n)
            case Radix.HEXADECIMAL:
                return '{:X}'.format(n)
            case Radix.DECIMAL:
                return str(n)

    @override
    def set_range(self, lower_bound: int, upper_bound: int) -> None:
        self.validator.set_range(lower_bound, upper_bound)
        value = min(max(self.value.get(), lower_bound), upper_bound)
        self.value.set(value)

    @override
    def set_value(self, value: int) -> None:
        if self.validator.lower_bound > value:
            raise ValueError(f'new_value must be greater than or equal to {self.validator.lower_bound}')
        if self.validator.upper_bound < value:
            raise ValueError(f'new_value must be less than or equal to {self.validator.upper_bound}')
        super().set_value(value)

    @property
    def radix(self) -> Radix:
        return self.validator.radix

    @radix.setter
    def radix(self, value: Radix) -> None:
        self.validator.radix = value
        new_text = self.to_string(self.value.get())
        self.setText(new_text)


class FloatInputField(NumericInputField[float]):
    def __init__(self, value: float = 0.0, parent: QObject | None = None):
        NumericInputField.__init__(self, FloatProperty(value), parent)
        self.validator: Final = QDoubleValidator()
        super().setValidator(self.validator)
        self.precision = 2

    @override
    def to_number(self, s: str | None) -> float | None:
        if s is None:
            return None
        return float(s)

    @override
    def to_string(self, n: float | None) -> str:
        if n is None:
            return ''
        return f'{n:.{self.precision}f}'

    @override
    def set_range(self, lower_bound: float, upper_bound: float) -> None:
        self.validator.setRange(lower_bound, upper_bound)
        value = min(max(self.value.get(), lower_bound), upper_bound)
        self.value.set(value)

    @override
    def set_value(self, value: float) -> None:
        if self.validator.bottom() > value:
            raise ValueError(f'new_value must be greater than or equal to {self.validator.bottom()}')
        if self.validator.top() < value:
            raise ValueError(f'new_value must be less than or equal to {self.validator.top()}')
        super().set_value(value)

    @property
    def precision(self) -> int:
        return self.__precision

    @precision.setter
    def precision(self, value: int) -> None:
        self.__precision = value
        new_text = self.to_string(self.value.get())
        self.setText(new_text)
