package com.github.dylmeadows.eontimer.model.settings;

import com.github.dylmeadows.eontimer.model.ThemeBackgroundMode;
import com.github.dylmeadows.eontimer.reference.settings.ThemeSettingsConstants;
import com.github.dylmeadows.eontimer.util.Styles;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class ThemeSettings implements ThemeSettingsConstants {

    private final ObjectProperty<ThemeBackgroundMode> backgroundMode;
    private final ObjectProperty<Color> backgroundColor;
    private final StringProperty backgroundImage;
    private final ObjectProperty<Color> panelBaseColor;
    private final DoubleProperty panelTransparency;
    private final ObjectProperty<Color> controlBaseColor;
    private final ObjectProperty<Color> labelTextColor;
    private final ObjectProperty<Color> accentColor;

    public ThemeSettings() {
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
        return Styles.toCss(this);
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
