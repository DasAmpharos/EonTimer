import functools
from typing import Final, TypeVar

from eon_timer.util import loggers
from eon_timer.util.enum import EnhancedEnum
from eon_timer.util.pyside import EnumComboBox

from .property import Property, PropertyChangeEvent

T = TypeVar('T')
EnhancedEnumT = TypeVar('EnhancedEnumT', bound=EnhancedEnum)

logger: Final = loggers.get_logger(__name__)


def bind(left: Property[T], right: Property[T], bidirectional: bool = True):
    def on_change(other: Property[T], event: PropertyChangeEvent[T]):
        other.set(event.new_value)

    left.update(right)
    right.on_change(functools.partial(on_change, left))
    if bidirectional:
        left.on_change(functools.partial(on_change, right))


def bind_enum_combobox(widget: EnumComboBox[EnhancedEnumT], p_property: Property[EnhancedEnumT]) -> None:
    logger.debug(f'bind_enum_combobox(widget={widget.objectName()}, property={p_property.get()})')
    widget.set_value(p_property.get())
    widget.value_changed.connect(p_property.set)
    p_property.on_change(lambda e: widget.set_value(e.new_value))
