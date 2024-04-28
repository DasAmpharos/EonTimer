import os
import platform
import subprocess
from typing import Final

from PySide6.QtCore import Qt
from PySide6.QtGui import QFont
from PySide6.QtWidgets import QComboBox, QPushButton, QWidget, QGridLayout, QLabel, QSizePolicy, QMessageBox

from eon_timer.theme.theme_manager import ThemeManager, ThemeError
from eon_timer.util.injector import component
from eon_timer.util.properties import bindings
from eon_timer.util.properties.property import Property
from eon_timer.util.properties.property_change import PropertyChangeEvent
from eon_timer.util.pyside.file_selector_widget import FileSelectorWidget
from .model import ThemeSettingsModel


@component()
class ThemeSettingsWidget(QWidget):
    def __init__(self,
                 model: ThemeSettingsModel,
                 theme_manager: ThemeManager) -> None:
        super().__init__()
        self.model: Final[ThemeSettingsModel] = model
        self.theme_manager: Final[ThemeManager] = theme_manager
        self.theme: Final = Property(model.theme.get())

        self.__theme_field: Final[QComboBox] = QComboBox()
        self.__import_theme_field: Final[FileSelectorWidget] = FileSelectorWidget()
        self.__import_btn: Final[QPushButton] = QPushButton()
        self.__init_components()

    def __init_components(self):
        # ----- layout -----
        layout = QGridLayout(self)
        layout.setAlignment(Qt.AlignmentFlag.AlignTop)
        layout.setContentsMargins(10, 10, 10, 10)
        # ----- theme_field -----
        self.__on_themes_changed()
        bindings.bind_str_combobox(self.__theme_field, self.theme)
        self.theme_manager.themes_changed.connect(self.__on_themes_changed)
        self.__theme_field.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Fixed)
        layout.addWidget(QLabel('Theme'), 0, 0)
        layout.addWidget(self.__theme_field, 0, 1)
        # ----- import_theme_field -----
        self.__import_theme_field.title = 'Select Theme'
        self.__import_theme_field.filter = 'Theme Files (*.zip)'
        self.__import_theme_field.file.on_change(self.__on_import_theme_file_changed)
        layout.addWidget(self.__import_theme_field, 1, 0, 1, 2)
        # ----- import_btn -----
        self.__import_btn.setText('Import Theme')
        layout.addWidget(self.__import_btn, 2, 1)
        self.__import_btn.clicked.connect(self.__on_import)
        self.__import_btn.setDisabled(True)
        # ----- open theme dir -----
        button = QPushButton(chr(0xf07b))
        button.setToolTip('Open Theme Directory')
        button.setFont(QFont('Font Awesome 5 Free'))
        button.clicked.connect(self.__open_theme_dir)
        layout.addWidget(button, 2, 0)

    def __on_themes_changed(self):
        self.__theme_field.clear()
        self.__theme_field.addItems(
            self.theme_manager.list_theme_names()
        )

    def __on_import(self):
        try:
            file = self.__import_theme_field.file.get()
            self.theme_manager.install_theme(file)
            self.__import_theme_field.file.set('')
        except Exception as e:
            QMessageBox.critical(self, 'Import Theme', f'Failed to import theme: {e}')

    def __on_import_theme_file_changed(self, event: PropertyChangeEvent[str]):
        self.__import_btn.setDisabled(not event.new_value)

    def __open_theme_dir(self):
        if platform.system() == 'Windows':
            os.startfile(self.theme_manager.user_theme_dir)
        elif platform.system() == 'Darwin':
            subprocess.Popen(['open', self.theme_manager.user_theme_dir])
        else:
            subprocess.Popen(['xdg-open', self.theme_manager.user_theme_dir])

    def on_accepted(self):
        self.model.theme.update(self.theme)

    def on_rejected(self):
        self.__reset_properties()

    def on_reset(self):
        self.model.reset()
        self.__reset_properties()

    def __reset_properties(self):
        self.theme.update(self.model.theme)
