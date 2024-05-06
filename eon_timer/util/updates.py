from typing import Final

import requests

from eon_timer import app
from eon_timer.settings.other.model import OtherSettingsModel
from eon_timer.util.injector import component


@component()
class UpdateChecker:
    def __init__(self,
                 other_settings: OtherSettingsModel):
        self.other_settings: Final = other_settings

    def check_for_updates(self):
        if app.is_bundled() and self.other_settings.check_for_updates.get():
            latest_version = UpdateChecker.get_latest_version()
            if latest_version is not None:
                current_version = app.get_version()
                if current_version != latest_version:
                    return f'New version available: {latest_version}'
        return None

    @staticmethod
    def get_latest_version() -> str | None:
        try:
            response = requests.get('https://api.github.com/repos/DasAmpharos/EonTimer/releases/latest', timeout=1.5)
            if response.status_code != 200:
                print(f'Error: {response.status_code}')
                return None
            response_body = response.json()
            return response_body.get('tag_name', None)
        except requests.exceptions.Timeout as error:
            print(f'Error: {error}')
            return None
