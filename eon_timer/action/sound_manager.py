from typing import Optional

from PySide6.QtCore import QUrl
from PySide6.QtMultimedia import QSoundEffect

from eon_timer import resources
from eon_timer.settings.action.model import ActionSound
from eon_timer.util.injector import component


@component()
class SoundManager:
    def __init__(self):
        def predefined_sound(filename: str) -> QSoundEffect:
            sound = QSoundEffect()
            filepath = resources.get_filepath(f'sounds/{filename}')
            url = QUrl.fromLocalFile(filepath)
            sound.setSource(url)
            return sound

        self.__sounds = {
            ActionSound.BEEP: predefined_sound('beep.wav'),
            ActionSound.DING: predefined_sound('ding.wav'),
            ActionSound.POP: predefined_sound('pop.wav'),
            ActionSound.TICK: predefined_sound('tick.wav'),
        }

    def get_sound(self, action_sound: ActionSound) -> Optional[QSoundEffect]:
        return self.__sounds.get(action_sound, None)

    def set_custom_sound(self, filepath: str) -> bool:
        sound = QSoundEffect()
        self.__sounds[ActionSound.CUSTOM] = sound
        url = QUrl.fromLocalFile(filepath)
        sound.setSource(url)
        return True
