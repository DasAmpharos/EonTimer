package com.github.dylmeadows.eontimer.reference.settings;

import com.github.dylmeadows.eontimer.reference.ActionMode;
import com.github.dylmeadows.eontimer.reference.resources.SoundResource;
import javafx.scene.paint.Color;

public interface ActionSettingsConstants {

    ActionMode DEFAULT_MODE = ActionMode.AUDIO;

    Color DEFAULT_COLOR = Color.CYAN;

    SoundResource DEFAULT_SOUND = SoundResource.BEEP;

    int DEFAULT_INTERVAL = 500;

    int DEFAULT_COUNT = 6;
}
