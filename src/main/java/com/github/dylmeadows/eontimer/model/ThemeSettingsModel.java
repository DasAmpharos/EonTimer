package com.github.dylmeadows.eontimer.model;

import com.github.dylmeadows.eontimer.util.StyleUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ThemeSettingsModel {

    private final ObjectProperty<ThemeBackgroundMode> backgroundMode = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_BACKGROUND_MODE);
    private final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_BACKGROUND_COLOR);
    private final StringProperty backgroundImage = new SimpleStringProperty(ThemeSettingsConstants.DEFAULT_BACKGROUND_IMAGE);
    private final ObjectProperty<Color> panelBaseColor = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_PANEL_BASE_COLOR);
    private final DoubleProperty panelTransparency = new SimpleDoubleProperty(ThemeSettingsConstants.DEFAULT_PANEL_TRANSPARENCY);
    private final ObjectProperty<Color> controlBaseColor = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_CONTROL_BASE_COLOR);
    private final ObjectProperty<Color> labelTextColor = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_LABEL_TEXT_COLOR);
    private final ObjectProperty<Color> accentColor = new SimpleObjectProperty<>(ThemeSettingsConstants.DEFAULT_ACCENT_COLOR);

    public StringBinding toCssBinding() {
        return Bindings.createStringBinding(this::toCss,
                backgroundMode, backgroundColor, backgroundImage,
                panelBaseColor, panelTransparency,
                controlBaseColor, labelTextColor,
                accentColor);
    }

    public String toCss() {
        return StyleUtils.toCss(this);
    }

    public ThemeBackgroundMode getBackgroundMode() {
        return backgroundMode.get();
    }

    public ObjectProperty<ThemeBackgroundMode> backgroundModeProperty() {
        return backgroundMode;
    }

    public void setBackgroundMode(ThemeBackgroundMode backgroundMode) {
        this.backgroundMode.set(backgroundMode);
    }

    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    public String getBackgroundImage() {
        return backgroundImage.get();
    }

    public StringProperty backgroundImageProperty() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage.set(backgroundImage);
    }

    public Color getPanelBaseColor() {
        return panelBaseColor.get();
    }

    public ObjectProperty<Color> panelBaseColorProperty() {
        return panelBaseColor;
    }

    public void setPanelBaseColor(Color panelBaseColor) {
        this.panelBaseColor.set(panelBaseColor);
    }

    public double getPanelTransparency() {
        return panelTransparency.get();
    }

    public DoubleProperty panelTransparencyProperty() {
        return panelTransparency;
    }

    public void setPanelTransparency(double panelTransparency) {
        this.panelTransparency.set(panelTransparency);
    }

    public Color getControlBaseColor() {
        return controlBaseColor.get();
    }

    public ObjectProperty<Color> controlBaseColorProperty() {
        return controlBaseColor;
    }

    public void setControlBaseColor(Color controlBaseColor) {
        this.controlBaseColor.set(controlBaseColor);
    }

    public Color getLabelTextColor() {
        return labelTextColor.get();
    }

    public ObjectProperty<Color> labelTextColorProperty() {
        return labelTextColor;
    }

    public void setLabelTextColor(Color labelTextColor) {
        this.labelTextColor.set(labelTextColor);
    }

    public Color getAccentColor() {
        return accentColor.get();
    }

    public ObjectProperty<Color> accentColorProperty() {
        return accentColor;
    }

    public void setAccentColor(Color accentColor) {
        this.accentColor.set(accentColor);
    }
}
