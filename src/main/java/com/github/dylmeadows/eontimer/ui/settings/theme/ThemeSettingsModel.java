package com.github.dylmeadows.eontimer.ui.settings.theme;

import com.github.dylmeadows.eontimer.model.ThemeBackgroundMode;
import com.github.dylmeadows.eontimer.reference.settings.ThemeSettingsConstants;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

/**
 * Settings related to theme specific configuration. Binds to
 * {@link ThemeSettingsView}.
 */
public class ThemeSettingsModel implements ThemeSettingsConstants {

    /**
     * Binds to {@link ThemeSettingsView#backgroundModeField}.
     */
    private final ObjectProperty<ThemeBackgroundMode> backgroundMode;

    /**
     * Binds to {@link ThemeSettingsView#backgroundColorField}.
     */
    private final ObjectProperty<Color> backgroundColor;

    /**
     * Binds to {@link ThemeSettingsView#backgroundImageField}.
     */
    private final StringProperty backgroundImage;

    private final ObjectProperty<Color> panelBaseColor;

    private final DoubleProperty panelTransparency;

    private final ObjectProperty<Color> controlBaseColor;

    private final ObjectProperty<Color> labelTextColor;

    private final ObjectProperty<Color> accentColor;

    /**
     * Initializes all properties to their default values.
     */
    public ThemeSettingsModel() {
        backgroundMode = new SimpleObjectProperty<>(DEFAULT_BACKGROUND_MODE);
        backgroundColor = new SimpleObjectProperty<>(DEFAULT_BACKGROUND_COLOR);
        backgroundImage = new SimpleStringProperty(DEFAULT_BACKGROUND_IMAGE);
        panelBaseColor = new SimpleObjectProperty<>(DEFAULT_PANEL_BASE_COLOR);
        panelTransparency = new SimpleDoubleProperty(DEFAULT_PANEL_TRANSPARENCY);
        controlBaseColor = new SimpleObjectProperty<>(DEFAULT_CONTROL_BASE_COLOR);
        labelTextColor = new SimpleObjectProperty<>(DEFAULT_LABEL_TEXT_COLOR);
        accentColor = new SimpleObjectProperty<>(DEFAULT_ACCENT_COLOR);
    }

    public StringBinding toCssBinding() {
        return Bindings.createStringBinding(this::toCss,
                backgroundMode, backgroundColor, backgroundImage,
                panelBaseColor, panelTransparency,
                controlBaseColor, labelTextColor,
                accentColor);
    }

    public String toCss() {
        return "";
    }

    /**
     * @return see {@link #backgroundMode}
     */
    public ThemeBackgroundMode getBackgroundMode() {
        return backgroundMode.get();
    }

    /**
     * @return see {@link #backgroundMode}
     */
    public ObjectProperty<ThemeBackgroundMode> backgroundModeProperty() {
        return backgroundMode;
    }

    /**
     * @param backgroundMode see {@link #backgroundMode}
     */
    public void setBackgroundMode(ThemeBackgroundMode backgroundMode) {
        this.backgroundMode.set(backgroundMode);
    }

    /**
     * @return see {@link #backgroundColor}
     */
    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    /**
     * @return see {@link #backgroundColor}
     */
    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor see {@link #backgroundColor}
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    /**
     * @return see {@link #backgroundImage}
     */
    public String getBackgroundImage() {
        return backgroundImage.get();
    }

    /**
     * @return see {@link #backgroundImage}
     */
    public StringProperty backgroundImageProperty() {
        return backgroundImage;
    }

    /**
     * @param backgroundImage see {@link #backgroundImage}
     */
    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage.set(backgroundImage);
    }

    /**
     * @return see {@link #panelBaseColor}
     */
    public Color getPanelBaseColor() {
        return panelBaseColor.get();
    }

    /**
     * @return see {@link #panelBaseColor}
     */
    public ObjectProperty<Color> panelBaseColorProperty() {
        return panelBaseColor;
    }

    /**
     * @param panelBaseColor see {@link #panelBaseColor}
     */
    public void setPanelBaseColor(Color panelBaseColor) {
        this.panelBaseColor.set(panelBaseColor);
    }

    /**
     * @return see {@link #panelTransparency}
     */
    public double getPanelTransparency() {
        return panelTransparency.get();
    }

    /**
     * @return see {@link #panelTransparency}
     */
    public DoubleProperty panelTransparencyProperty() {
        return panelTransparency;
    }

    /**
     * @param panelTransparency see {@link #panelTransparency}
     */
    public void setPanelTransparency(double panelTransparency) {
        this.panelTransparency.set(panelTransparency);
    }

    /**
     * @return see {@link #controlBaseColor}
     */
    public Color getControlBaseColor() {
        return controlBaseColor.get();
    }

    /**
     * @return see {@link #controlBaseColor}
     */
    public ObjectProperty<Color> controlBaseColorProperty() {
        return controlBaseColor;
    }

    /**
     * @param controlBaseColor see {@link #controlBaseColor}
     */
    public void setControlBaseColor(Color controlBaseColor) {
        this.controlBaseColor.set(controlBaseColor);
    }

    /**
     * @return see {@link #labelTextColor}
     */
    public Color getLabelTextColor() {
        return labelTextColor.get();
    }

    /**
     * @return see {@link #labelTextColor}
     */
    public ObjectProperty<Color> labelTextColorProperty() {
        return labelTextColor;
    }

    /**
     * @param labelTextColor see {@link #labelTextColor}
     */
    public void setLabelTextColor(Color labelTextColor) {
        this.labelTextColor.set(labelTextColor);
    }

    /**
     * @return see {@link #accentColor}
     */
    public Color getAccentColor() {
        return accentColor.get();
    }

    /**
     * @return see {@link #accentColor}
     */
    public ObjectProperty<Color> accentColorProperty() {
        return accentColor;
    }

    /**
     * @param accentColor see {@link #accentColor}
     */
    public void setAccentColor(Color accentColor) {
        this.accentColor.set(accentColor);
    }
}
