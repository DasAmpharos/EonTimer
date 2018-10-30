package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.eontimer.model.ThemeBackgroundMode;
import javafx.scene.paint.Color;

public interface ThemeSettingsConstants {

    ThemeBackgroundMode DEFAULT_BACKGROUND_MODE = ThemeBackgroundMode.DEFAULT;

    Color DEFAULT_BACKGROUND_COLOR = Color.valueOf("#63B2C4");

    String DEFAULT_BACKGROUND_IMAGE = "";

    Color DEFAULT_PANEL_BASE_COLOR = Color.valueOf("#FFFFFF");

    double DEFAULT_PANEL_TRANSPARENCY = 0.5;

    Color DEFAULT_CONTROL_BASE_COLOR = Color.valueOf("#BFBFBF");

    Color DEFAULT_LABEL_TEXT_COLOR = Color.BLACK;

    Color DEFAULT_ACCENT_COLOR = Color.valueOf("#039ED3");
}
