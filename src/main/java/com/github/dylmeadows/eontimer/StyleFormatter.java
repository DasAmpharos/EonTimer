package com.github.dylmeadows.eontimer;

import com.github.dylmeadows.common.javafx.paint.Colors;
import com.github.dylmeadows.eontimer.reference.resources.ImageResource;
import com.github.dylmeadows.eontimer.ui.settings.theme.ThemeSettingsModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class StyleFormatter {

    private static final String THEME_ACCENT = "-theme-accent";
    private static final String THEME_FAINT_ACCENT = "-theme-faint-accent";
    private static final String THEME_PANEL_BASE = "-theme-panel-base";
    private static final String THEME_PANEL_TRANSPARENT_BASE = "-theme-panel-transparent-base";
    private static final String THEME_CONTROL_BASE = "-theme-control-base";
    private static final String THEME_LABEL_TEXT = "-theme-label-text";
    private static final String THEME_DEFAULT_BUTTON_BASE = "-theme-default-button-base";
    private static final String FX_BACKGROUND_COLOR = "-fx-background-color";
    private static final String FX_BACKGROUND_IMAGE = "-fx-background-image";
    private static final String FX_BACKGROUND_SIZE = "-fx-background-size";

    private static final double THEME_FAINT_ACCENT_ALPHA = 0.13;
    private static final double THEME_DEFAULT_BUTTON_BASE_SATURATION_TRANSFORM = -0.35;
    private static final double THEME_DEFAULT_BUTTON_BASE_LIGHTNESS_TRANSFORM = 0.4;
    private static final String BACKGROUND_IMAGE_SIZE = "cover";

    private StyleFormatter() {
    }

    public static String toCss(ThemeSettingsModel theme) {
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
        return css.keySet().stream()
                .map(key -> String.join(": ", key, css.get(key)))
                .collect(Collectors.joining("; "));
    }

    private static String getBackgroundColor(ThemeSettingsModel theme) {
        return Colors.toHex(theme.getBackgroundColor());
    }

    private static String getDefaultBackgroundImage() {
        return "url(/" + ImageResource.DEFAULT_BACKGROUND_IMAGE.getPath() + ")";
    }

    private static String getBackgroundImage(ThemeSettingsModel theme) {
        File file = new File(theme.getBackgroundImage());
        return "url(\"" + file.toURI() + "\")";
    }

    private static String getThemeAccent(ThemeSettingsModel theme) {
        return Colors.toHex(theme.getAccentColor());
    }

    private static String getThemeFaintAccent(ThemeSettingsModel theme) {
        return Colors.toHexAlpha(theme.getAccentColor(), THEME_FAINT_ACCENT_ALPHA);
    }

    private static String getThemePanelBase(ThemeSettingsModel theme) {
        return Colors.toHex(theme.getPanelBaseColor());
    }

    private static String getThemePanelTransparentBase(ThemeSettingsModel theme) {
        return Colors.toHexAlpha(theme.getPanelBaseColor(), theme.getPanelTransparency());
    }

    private static String getThemeControlBase(ThemeSettingsModel theme) {
        return Colors.toHex(theme.getControlBaseColor());
    }

    private static String getThemeLabelText(ThemeSettingsModel theme) {
        return Colors.toHex(theme.getLabelTextColor());
    }

    private static String getThemeDefaultButtonBase(ThemeSettingsModel theme) {
        double[] hsla = Colors.toHSLA(theme.getAccentColor());
        hsla = Colors.deriveSaturation(hsla, THEME_DEFAULT_BUTTON_BASE_SATURATION_TRANSFORM);
        hsla = Colors.deriveLightness(hsla, THEME_DEFAULT_BUTTON_BASE_LIGHTNESS_TRANSFORM);
        return Colors.toHex(Colors.toColor(hsla));
    }
}
