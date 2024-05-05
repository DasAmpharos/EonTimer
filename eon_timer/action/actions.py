from typing import Callable, Final

from eon_timer.app_state import AppState
from eon_timer.settings.action.model import ActionMode, ActionSettingsModel, ActionSound
from eon_timer.util.injector import component
from .sound_manager import SoundManager
from .visual_manager import VisualManager


@component()
class Actions:
    def __init__(self,
                 state: AppState,
                 sound_manager: SoundManager,
                 visual_manager: VisualManager,
                 action_settings: ActionSettingsModel):
        self.sound_manager: Final = sound_manager
        self.visual_manager: Final = visual_manager
        self.action_settings: Final = action_settings
        self.__audio_action: Callable = self.__do_nothing
        self.__visual_action: Callable = self.__do_nothing
        action_settings.settings_changed.connect(self.__on_action_settings_changed)
        state.action_triggered.connect(self.trigger)
        self.__on_action_settings_changed()

    def __on_action_settings_changed(self):
        action_mode = self.action_settings.mode.get()
        action_sound = self.action_settings.sound.get()

        # update sound
        if action_sound == ActionSound.CUSTOM:
            self.sound_manager.set_custom_sound(self.action_settings.custom_sound.get())
        sound = self.sound_manager.get_sound(action_sound)

        # update audio action
        audio_action = self.__do_nothing
        if sound is not None and (action_mode == ActionMode.AV or action_mode == ActionMode.AUDIO):
            audio_action = sound.play
        self.__audio_action = audio_action
        # update visual action
        visual_action = self.__do_nothing
        if action_mode == ActionMode.AV or action_mode == ActionMode.VISUAL:
            visual_action = self.visual_manager.activate
        self.__visual_action = visual_action

    def trigger(self):
        self.__audio_action()
        self.__visual_action()

    @staticmethod
    def __do_nothing():
        pass
