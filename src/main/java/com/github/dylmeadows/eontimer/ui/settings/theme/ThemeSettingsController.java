package com.github.dylmeadows.eontimer.ui.settings.theme;

import com.github.dylmeadows.common.javafx.BindableController;

public class ThemeSettingsController extends BindableController<ThemeSettingsModel, ThemeSettingsView> {

    public ThemeSettingsController(ThemeSettingsModel model, ThemeSettingsView view) {
        super(model, view, true);
    }

    @Override
    protected void bind(ThemeSettingsModel model, ThemeSettingsView view) {
        view.backgroundModeProperty().bindBidirectional(model.backgroundModeProperty());
        view.backgroundColorProperty().bindBidirectional(model.backgroundColorProperty());
        view.backgroundImageProperty().bindBidirectional(model.backgroundImageProperty());
        view.panelBaseColorProperty().bindBidirectional(model.panelBaseColorProperty());
        view.panelTransparencyProperty().bindBidirectional(model.panelTransparencyProperty());
        view.controlBaseColorProperty().bindBidirectional(model.controlBaseColorProperty());
        view.labelTextColorProperty().bindBidirectional(model.labelTextColorProperty());
        view.accentColorProperty().bindBidirectional(model.accentColorProperty());
    }

    @Override
    protected void unbind(ThemeSettingsModel model, ThemeSettingsView view) {
        view.backgroundModeProperty().unbindBidirectional(model.backgroundModeProperty());
        view.backgroundColorProperty().unbindBidirectional(model.backgroundColorProperty());
        view.backgroundImageProperty().unbindBidirectional(model.backgroundImageProperty());
        view.panelBaseColorProperty().unbindBidirectional(model.panelBaseColorProperty());
        view.panelTransparencyProperty().unbindBidirectional(model.panelTransparencyProperty());
        view.controlBaseColorProperty().unbindBidirectional(model.controlBaseColorProperty());
        view.labelTextColorProperty().unbindBidirectional(model.labelTextColorProperty());
        view.accentColorProperty().unbindBidirectional(model.accentColorProperty());
    }
}
