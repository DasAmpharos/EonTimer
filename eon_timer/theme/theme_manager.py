import atexit
import json
import os
import re
import shutil
import tempfile
import zipfile
from dataclasses import dataclass
from typing import Final, override

import platformdirs
import sass
from PySide6.QtCore import QObject, Signal
from PySide6.QtWidgets import QApplication

from eon_timer import resources
from eon_timer.util import pyside
from eon_timer.util.injector import component
from eon_timer.util.injector.lifecycle import StartListener


@dataclass
class ThemeInfo:
    name: str
    author: str
    version: str


@dataclass
class Theme:
    info: ThemeInfo
    stylesheet: str


@dataclass
class InstalledTheme(Theme):
    location: str


@component()
class ThemeManager(QObject, StartListener):
    themes_changed: Final[Signal] = Signal()

    DEFAULT_THEME: Final[str] = 'Default'
    SYSTEM_THEME: Final[str] = 'System'

    def __init__(self, app: QApplication) -> None:
        super().__init__(None)
        self.data_dir: Final[str] = platformdirs.user_data_dir(appname=app.applicationName(),
                                                               appauthor=app.organizationName(),
                                                               ensure_exists=True)
        self.theme_dir: Final[str] = os.path.join(self.data_dir, 'themes')
        self.__themes: Final[dict[str, Theme]] = {}

    @override
    def _on_start(self):
        os.makedirs(self.theme_dir, exist_ok=True)
        pyside.install_font(resources.get_bytes('fonts/FontAwesome.ttf'))
        self.__default_theme = self.__install_theme(resources.get_filepath('theme.zip'), self.data_dir)
        self.__system_theme = Theme(ThemeInfo(name=self.SYSTEM_THEME, author='', version=''), '')
        self.__load_themes()

    def list_theme_names(self) -> list[str]:
        theme_names = [self.DEFAULT_THEME, self.SYSTEM_THEME]
        theme_names.extend(self.__themes.keys())
        return theme_names

    def install_theme(self, filepath: str) -> InstalledTheme:
        theme = self.__install_theme(filepath, self.theme_dir)
        self.__themes[theme.info.name] = theme
        self.themes_changed.emit()
        return theme

    def __install_theme(self, filepath: str, install_location: str) -> InstalledTheme:
        temp_dir = tempfile.mkdtemp()
        atexit.register(shutil.rmtree, temp_dir)
        with zipfile.ZipFile(filepath, 'r') as zip_file:
            zip_file.extractall(temp_dir)
        # validate theme info
        info_file = os.path.join(temp_dir, 'info.json')
        if not os.path.exists(info_file):
            raise ThemeError('Theme is missing info.json')
        with open(info_file, 'r') as file:
            theme_info = json.load(file)
            theme_info = ThemeInfo(**theme_info)
        # validate main.scss
        stylesheet_path = os.path.join(temp_dir, 'main.scss')
        if not os.path.exists(stylesheet_path):
            raise ThemeError('Theme is missing a main.scss file')

        # install theme to user_data_dir
        theme_dir = os.path.join(install_location, theme_info.name)
        shutil.copytree(temp_dir, theme_dir, dirs_exist_ok=True)

        # load any fonts
        self.__load_fonts(theme_dir)
        # load main.scss
        stylesheet_path = os.path.join(theme_dir, 'main.scss')
        with open(stylesheet_path, 'r') as file:
            stylesheet = file.read()
        # substitute any variables
        for variable in set(re.findall(r'#\((.*?)\)', stylesheet)):
            variable_filepath = os.path.join(theme_dir, variable)
            if not os.path.exists(variable_filepath):
                raise ThemeError(f'File {variable_filepath} is defined in stylesheet but not included')
            variable_filepath = resources.normalize_filepath(variable_filepath)
            stylesheet = stylesheet.replace(f'#({variable})', variable_filepath)

        # compile stylesheet
        stylesheet = sass.compile(string=stylesheet)
        stylesheet_filepath = os.path.join(theme_dir, 'main.css')
        with open(stylesheet_filepath, 'w') as file:
            file.write(stylesheet)
        return InstalledTheme(theme_info, stylesheet, theme_dir)

    def get_theme(self, theme_name: str) -> Theme:
        match theme_name:
            case self.DEFAULT_THEME:
                return self.__default_theme
            case self.SYSTEM_THEME:
                return self.__system_theme
            case _:
                return self.__themes.get(theme_name, self.__default_theme)

    def __load_themes(self):
        self.__themes.clear()
        for theme_dir in os.listdir(self.theme_dir):
            if os.path.isdir(os.path.join(self.theme_dir, theme_dir)):
                theme_info = self.__load_theme_info(theme_dir)
                if theme_info.name not in self.__themes:
                    theme = self.__load_theme(theme_dir, theme_info)
                    self.__themes[theme_info.name] = theme

    def __load_theme(self, theme_name: str, info: ThemeInfo | None = None) -> InstalledTheme:
        if info is None:
            info = self.__load_theme_info(theme_name)
        theme_dir = os.path.join(self.theme_dir, theme_name)
        self.__load_fonts(theme_dir)
        # load main.css
        stylesheet_path = os.path.join(theme_dir, 'main.css')
        with open(stylesheet_path, 'r') as file:
            stylesheet = file.read()
        return InstalledTheme(info, stylesheet, theme_name)

    def __load_theme_info(self, theme_name: str) -> ThemeInfo:
        theme_dir = os.path.join(self.theme_dir, theme_name)
        info_file = os.path.join(theme_dir, 'info.json')
        with open(info_file, 'r') as file:
            theme_info = json.load(file)
            theme_info = ThemeInfo(**theme_info)
        return theme_info

    def __load_fonts(self, dirname: str):
        fonts_dir = os.path.join(dirname, 'fonts')
        if os.path.exists(fonts_dir):
            for font in os.listdir(fonts_dir):
                font_filepath = os.path.join(fonts_dir, font)
                with open(font_filepath, 'rb') as file:
                    font_data = file.read()
                pyside.install_font(font_data)


class ThemeError(Exception):
    pass
