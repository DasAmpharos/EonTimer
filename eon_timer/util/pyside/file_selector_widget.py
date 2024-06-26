import os.path
from typing import Callable, Final, Optional

from PySide6.QtWidgets import QFileDialog, QHBoxLayout, QLineEdit, QPushButton, QWidget

from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property


class FileSelectorWidget(QWidget):
    def __init__(self,
                 title: str = 'Select File',
                 filter: str | None = None,
                 file: str | None = None,
                 validator: Optional[Callable[[str], bool]] = None,
                 parent: Optional[QWidget] = None):
        super().__init__(parent)
        self.title: Optional[str] = title
        self.filter: Optional[str] = filter
        self.file: Final = Property(file or '')
        self.file_validator: Optional[Callable[[str], bool]] = validator

        self.__line_edit: Final = QLineEdit()
        self.__button: Final = QPushButton('...')
        bindings.bind_line_edit(self.__line_edit, self.file)
        self.__init_components()

    def __init_components(self):
        layout = QHBoxLayout(self)
        layout.setContentsMargins(0, 0, 0, 0)
        layout.setSpacing(0)

        self.__line_edit.setProperty('class', 'file-selector-field')
        self.__button.setProperty('class', 'file-selector-button')
        self.__button.clicked.connect(self.__on_button_clicked)
        self.__line_edit.setDisabled(True)
        layout.addWidget(self.__line_edit)
        layout.addWidget(self.__button)

    def __on_button_clicked(self):
        filename = self.file.get()
        dirname = os.path.dirname(filename) if filename else os.curdir

        args = {'parent': self, 'caption': self.title, 'dir': dirname}
        if self.filter is not None:
            args['filter'] = self.filter

        filename, _ = QFileDialog.getOpenFileName(**args)
        is_valid = True
        if self.file_validator is not None:
            is_valid = self.file_validator(filename)
        if is_valid:
            self.file.set(filename)
