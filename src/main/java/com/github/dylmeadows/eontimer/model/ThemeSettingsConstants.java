package com.github.dylmeadows.eontimer.model;

import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class ThemeSettingsConstants {
    public final ThemeBackgroundMode DEFAULT_BACKGROUND_MODE = ThemeBackgroundMode.DEFAULT;
    public final Color DEFAULT_BACKGROUND_COLOR = Color.valueOf("#63B2C4");
    public final String DEFAULT_BACKGROUND_IMAGE = "";
    public final Color DEFAULT_PANEL_BASE_COLOR = Color.valueOf("#FFFFFF");
    public final double DEFAULT_PANEL_TRANSPARENCY = 0.5;
    public final Color DEFAULT_CONTROL_BASE_COLOR = Color.valueOf("#BFBFBF");
    public final Color DEFAULT_LABEL_TEXT_COLOR = Color.BLACK;
    public final Color DEFAULT_ACCENT_COLOR = Color.valueOf("#039ED3");
}
