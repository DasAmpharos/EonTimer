package com.github.dylmeadows.eontimer.ui.settings.action;

import com.github.dylmeadows.common.javafx.BindableController;

public class ActionSettingsController extends BindableController<ActionSettingsModel, ActionSettingsView> {

    public ActionSettingsController(ActionSettingsModel model, ActionSettingsView view) {
        super(model, view, true);
    }

    @Override
    protected void bind(ActionSettingsModel model, ActionSettingsView view) {
        view.modeProperty().bindBidirectional(model.modeProperty());
        view.colorProperty().bindBidirectional(model.colorProperty());
        view.soundProperty().bindBidirectional(model.soundProperty());
        view.intervalProperty().bindBidirectional(model.intervalProperty());
        view.countProperty().bindBidirectional(model.countProperty());
    }

    @Override
    protected void unbind(ActionSettingsModel model, ActionSettingsView view) {
        view.modeProperty().unbindBidirectional(model.modeProperty());
        view.colorProperty().unbindBidirectional(model.colorProperty());
        view.soundProperty().unbindBidirectional(model.soundProperty());
        view.intervalProperty().unbindBidirectional(model.intervalProperty());
        view.countProperty().unbindBidirectional(model.countProperty());
    }
}
