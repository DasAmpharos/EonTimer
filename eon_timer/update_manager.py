from typing import Final, Optional

import requests
from PySide6.QtGui import QDesktopServices
from PySide6.QtWidgets import QMessageBox, QWidget

from eon_timer import app
from eon_timer.settings.other.update_model import UpdateSettingsModel
from eon_timer.util.injector import component


@component()
class UpdateManager:
    def __init__(self, update_settings: UpdateSettingsModel):
        self.update_settings: Final = update_settings

    def check_for_updates(self, parent: QWidget):
        latest_release = self.get_latest_release() or {}
        tag_name = latest_release.get('tag_name', None)
        if tag_name is not None:
            current_version = app.get_version()
            if tag_name != current_version and tag_name != self.update_settings.acknowledged.get():
                message = f'A new version of EonTimer is available: {tag_name}'
                reply = QMessageBox.information(parent, 'Update Available', message,
                                                QMessageBox.StandardButton.Open | QMessageBox.StandardButton.Ignore,
                                                QMessageBox.StandardButton.Open)
                if reply == QMessageBox.StandardButton.Open:
                    html_url = latest_release.get('html_url', '')
                    QDesktopServices.openUrl(html_url)
                elif reply == QMessageBox.StandardButton.Ignore:
                    self.update_settings.acknowledged.set(tag_name)

    @staticmethod
    def get_latest_release() -> Optional[any]:
        try:
            response = requests.get('https://api.github.com/repos/DasAmpharos/EonTimer/releases/latest', timeout=1.5)
            if response.status_code != 200:
                print(f'Error: {response.status_code}')
                print(f'Error: {response.content.decode()}')
                print(f'Error: {response.headers}')
                return None
            return response.json()
        except requests.exceptions.Timeout as error:
            print(f'Error: {error}')
            return None

    @property
    def check_on_startup(self) -> bool:
        return self.update_settings.check_on_startup.get()
