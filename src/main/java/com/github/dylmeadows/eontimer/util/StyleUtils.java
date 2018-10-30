package com.github.dylmeadows.eontimer.util;

import com.github.dylmeadows.eontimer.model.resources.ImageResource;
import com.github.dylmeadows.eontimer.model.settings.ThemeSettings;
import com.github.dylmeadows.javafx.scene.paint.ColorUtils;
import javafx.scene.paint.Color;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
@SuppressWarnings({"unused", "WeakerAccess"})
public class StyleUtils {

    private final String THEME_ACCENT = "-theme-accent";
    private final String THEME_FAINT_ACCENT = "-theme-faint-accent";
    private final String THEME_PANEL_BASE = "-theme-panel-base";
    private final String THEME_PANEL_TRANSPARENT_BASE = "-theme-panel-transparent-base";
    private final String THEME_CONTROL_BASE = "-theme-control-base";
    private final String THEME_LABEL_TEXT = "-theme-label-text";
    private final String THEME_DEFAULT_BUTTON_BASE = "-theme-default-button-base";
    private final String FX_BACKGROUND_COLOR = "-fx-background-color";
    private final String FX_BACKGROUND_IMAGE = "-fx-background-image";
    private final String FX_BACKGROUND_SIZE = "-fx-background-size";

    private final double THEME_FAINT_ACCENT_ALPHA = 0.13;
    private final double THEME_DEFAULT_BUTTON_BASE_SATURATION_TRANSFORM = -0.35;
    private final double THEME_DEFAULT_BUTTON_BASE_BRIGHTNESS_TRANSFORM = 0.4;
    private final String BACKGROUND_IMAGE_SIZE = "cover";

    public String toCss(ThemeSettings theme) {
        Map<String, String> css = new HashMap<>();
        css.put(THEME_ACCENT, getThemeAccent(theme));
        css.put(THEME_FAINT_ACCENT, getThemeFaintAccent(theme));
        css.put(THEME_PANEL_BASE, getThemePanelBase(theme));
        css.put(THEME_PANEL_TRANSPARENT_BASE, getThemePanelTransparentBase(theme));
        css.put(THEME_CONTROL_BASE, getThemeControlBase(theme));
        css.put(THEME_LABEL_TEXT, getThemeLabelText(theme));
        css.put(THEME_DEFAULT_BUTTON_BASE, getThemeDefaultButtonBase(theme));

        switch (theme.getBackgroundMode()) {
            case DEFAULT:
                css.put(FX_BACKGROUND_IMAGE, getDefaultBackgroundImage());
                css.put(FX_BACKGROUND_SIZE, BACKGROUND_IMAGE_SIZE);
                break;
            case COLOR:
                css.put(FX_BACKGROUND_COLOR, getBackgroundColor(theme));
                break;
            case IMAGE:
                if (!theme.getBackgroundImage().isEmpty()) {
                    css.put(FX_BACKGROUND_IMAGE, getBackgroundImage(theme));
                    css.put(FX_BACKGROUND_SIZE, BACKGROUND_IMAGE_SIZE);
                }
                break;
        }

        return css.entrySet().stream()
                .map(entry -> String.join(": ", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("; "));
    }

    private String getBackgroundColor(ThemeSettings theme) {
        return ColorUtils.toHex(theme.getBackgroundColor());
    }

    private String getDefaultBackgroundImage() {
        return "url(/" + ImageResource.DEFAULT_BACKGROUND_IMAGE.getPath() + ")";
    }

    private String getBackgroundImage(ThemeSettings theme) {
        File file = new File(theme.getBackgroundImage());
        return "url(\"" + file.toURI() + "\")";
    }

    private String getThemeAccent(ThemeSettings theme) {
        return ColorUtils.toHex(theme.getAccentColor());
    }

    private String getThemeFaintAccent(ThemeSettings theme) {
        return ColorUtils.toHexAlpha(theme.getAccentColor(), THEME_FAINT_ACCENT_ALPHA);
    }

    private String getThemePanelBase(ThemeSettings theme) {
        return ColorUtils.toHex(theme.getPanelBaseColor());
    }

    private String getThemePanelTransparentBase(ThemeSettings theme) {
        return ColorUtils.toHexAlpha(theme.getPanelBaseColor(), theme.getPanelTransparency());
    }

    private String getThemeControlBase(ThemeSettings theme) {
        return ColorUtils.toHex(theme.getControlBaseColor());
    }

    private String getThemeLabelText(ThemeSettings theme) {
        return ColorUtils.toHex(theme.getLabelTextColor());
    }

    private String getThemeDefaultButtonBase(ThemeSettings theme) {
        Color color = theme.getAccentColor();
        color = ColorUtils.deriveSaturation(color, THEME_DEFAULT_BUTTON_BASE_SATURATION_TRANSFORM);
        color = ColorUtils.deriveBrightness(color, THEME_DEFAULT_BUTTON_BASE_BRIGHTNESS_TRANSFORM);
        return ColorUtils.toHex(color);
    }
}
