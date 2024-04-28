from typing import Optional

import pygame.mixer

from eon_timer import resources
from eon_timer.settings.action.model import ActionSound
from eon_timer.util.injector import component


@component()
class SoundManager:
    def __init__(self):
        def predefined_sound(filename: str) -> pygame.mixer.Sound:
            filepath = resources.get_filepath(f'sounds/{filename}')
            return pygame.mixer.Sound(filepath)

        pygame.mixer.init()
        self.__sounds = {
            ActionSound.BEEP: predefined_sound('beep.wav'),
            ActionSound.DING: predefined_sound('ding.wav'),
            ActionSound.POP: predefined_sound('pop.wav'),
            ActionSound.TICK: predefined_sound('tick.wav'),
        }

    def get_sound(self, action_sound: ActionSound) -> Optional[pygame.mixer.Sound]:
        return self.__sounds.get(action_sound, None)

    def set_custom_sound(self, filepath: str) -> bool:
        try:
            sound = pygame.mixer.Sound(filepath)
            self.__sounds[ActionSound.CUSTOM] = sound
            return True
        except pygame.error:
            return False
