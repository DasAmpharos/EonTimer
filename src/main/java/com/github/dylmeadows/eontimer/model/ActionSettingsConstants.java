package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.eontimer.model.ActionMode;
import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import javafx.scene.paint.Color;

public interface ActionSettingsConstants {

    ActionMode DEFAULT_MODE = ActionMode.AUDIO;

    Color DEFAULT_COLOR = Color.CYAN;

    SoundResource DEFAULT_SOUND = SoundResource.BEEP;

    int DEFAULT_INTERVAL = 500;

    int DEFAULT_COUNT = 6;
}
