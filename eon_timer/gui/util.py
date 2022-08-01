from PySide6.QtWidgets import QWidget


def set_class(self: QWidget, classes: list[str]) -> None:
    self.setProperty('class', ' '.join(classes))
