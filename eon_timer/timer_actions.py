from typing import Final, Callable, Optional

import pkg_resources
import pygame
from PySide6.QtGui import QColor

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionSettingsModel, ActionSound, ActionMode
from eon_timer.util.injector import component
from eon_timer.util.properties.property_change import PropertyChangeEvent


@component()
class TimerActions:
    def __init__(self,
                 state: AppState,
                 action_settings: ActionSettingsModel):
        self.__audio_action: Optional[Callable] = None
        self.__visual_action: Optional[Callable] = None
        self.__sound: Optional[pygame.mixer.Sound] = None
        self.action_settings: Final[ActionSettingsModel] = action_settings
        action_settings.sound.on_change(self.__on_sound_changed)
        action_settings.color.on_change(self.__on_color_changed)
        state.action_triggered.connect(self.__trigger)

        pygame.mixer.init()
        self.__predefined_sounds = {
            ActionSound.BEEP: self.__load_sound('eon_timer.resources.sounds', 'beep.wav'),
            ActionSound.DING: self.__load_sound('eon_timer.resources.sounds', 'ding.wav'),
            ActionSound.POP: self.__load_sound('eon_timer.resources.sounds', 'pop.wav'),
            ActionSound.TICK: self.__load_sound('eon_timer.resources.sounds', 'tick.wav'),
        }
        self.__on_sound_changed(PropertyChangeEvent(None, action_settings.sound.get()))
        self.__on_mode_changed(PropertyChangeEvent(None, action_settings.mode.get()))

    def __on_mode_changed(self, event: PropertyChangeEvent[ActionMode]):
        audio_action: Callable = lambda: None
        if event.new_value == ActionMode.AUDIO or event.new_value == ActionMode.AV:
            audio_action = self.__sound.play
        self.__audio_action = audio_action

    def __on_sound_changed(self, event: PropertyChangeEvent[ActionSound]):
        sound = self.__predefined_sounds.get(event.new_value, None)
        if sound is None and event.new_value == ActionSound.CUSTOM:
            sound = pygame.mixer.Sound(self.action_settings.custom_sound.get())
        self.__sound = sound

    def __on_color_changed(self, event: PropertyChangeEvent[QColor]):
        pass

    @staticmethod
    def __load_sound(package: str, filename: str):
        resource_filename = pkg_resources.resource_filename(package, filename)
        return pygame.mixer.Sound(resource_filename)

    def __trigger(self):
        self.__audio_action()
