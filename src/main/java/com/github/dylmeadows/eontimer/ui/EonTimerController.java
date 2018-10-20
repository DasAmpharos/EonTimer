package com.github.dylmeadows.eontimer.ui;

import com.github.dylmeadows.eontimer.util.ResourceBundles;
import com.github.dylmeadows.eontimer.handlers.*;
import com.github.dylmeadows.eontimer.handlers.actions.SoundAction;
import com.github.dylmeadows.eontimer.handlers.actions.VisualAction;
import com.github.dylmeadows.eontimer.model.ActionMode;
import com.github.dylmeadows.eontimer.ui.settings.EonTimerSettingsController;
import com.github.dylmeadows.eontimer.ui.timers.custom.CustomTimerController;
import com.github.dylmeadows.eontimer.ui.timers.gen3.Gen3TimerController;
import com.github.dylmeadows.eontimer.ui.timers.gen4.Gen4TimerController;
import com.github.dylmeadows.eontimer.ui.timers.gen5.Gen5TimerController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Dialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ResourceBundle;

public class EonTimerController {

    private final TimerMonitor monitor;
    private final ActionHandler actionHandler;
    private final VisualAction visualAction;
    private final SoundAction soundAction;
    // TODO: refactor
    /*private final DisplayHandler displayHandler;*/

    private final Gen5TimerController gen5TimerController;
    private final Gen4TimerController gen4TimerController;
    private final Gen3TimerController gen3TimerController;
    private final CustomTimerController customTimerController;
    private final EonTimerSettingsController eonTimerSettingsController;

    // region TODO: add variable target
    // private final BooleanBinding isVariableTargetTimer;
    // private final BooleanBinding isTargetFrameSet;
    // endregion

    private final ChangeListener<ActionMode> actionModeChangeListener;
    private final ChangeListener<EonTimerView.TimerType> timerTypeChangeListener;
    private final StringBinding timerBtnTextBinding;

    @Autowired
    public EonTimerController(EonTimerModel model, EonTimerView view) {
        // ===== monitor =====
        monitor = new TimerMonitor();
        // ===== actionHandler =====
        actionHandler = new ActionHandler();
        monitor.addListener(actionHandler);
        // ===== visualAction =====
        visualAction = new VisualAction();
        // ===== soundAction =====
        soundAction = new SoundAction();
        // ===== displayHandler =====
        // TODO: refactor
        /*displayHandler = new DisplayHandler(monitor);*/
        /*monitor.addListener((TimerLifecycleListener) displayHandler);
        monitor.addListener((TimerStageLifecycleListener) displayHandler);*/

        gen5TimerController = new Gen5TimerController(model.getGen5TimerModel(), view.getGen5TimerView());
        gen5TimerController.allFieldsDisableProperty().bind(monitor.runningProperty());
        gen4TimerController = new Gen4TimerController(model.getGen4TimerModel(), view.getGen4TimerView());
        gen4TimerController.allFieldsDisableProperty().bind(monitor.runningProperty());
        gen3TimerController = new Gen3TimerController(model.getGen3TimerModel(), view.getGen3TimerView());
        gen3TimerController.allFieldsDisableProperty().bind(monitor.runningProperty());
        customTimerController = new CustomTimerController(model.getCustomTimerModel(), view.getCustomTimerView());
        customTimerController.allFieldsDisableProperty().bind(monitor.runningProperty());
        eonTimerSettingsController = new EonTimerSettingsController(model.getEonTimerSettingsModel(), view.getEonTimerSettingsView());

        // region TODO: add variable target
        // IntegerProperty targetFrame = new SimpleIntegerProperty(-1);
        // isVariableTargetTimer = Bindings.createBooleanBinding(() -> {
        //     return monitor.getTimer() instanceof VariableTargetFrameTimer;
        // }, monitor.timerProperty());
        // isVariableTargetTimer.addListener((observable, oldValue, newValue) -> {
        //     targetFrame.unbind();
        //     if (newValue) {
        //         VariableTargetFrameTimer timer = (VariableTargetFrameTimer) monitor.getTimer();
        //         targetFrame.bind(timer.targetFrameProperty());
        //     } else {
        //         targetFrame.set(-1);
        //     }
        // });
        // isTargetFrameSet = targetFrame.isNotEqualTo(-1);
        // endregion

        actionModeChangeListener = this::onActionModeChange;
        timerTypeChangeListener = this::onTimerTypeChange;
        timerBtnTextBinding = Bindings.createStringBinding(this::computeTimerBtnText,
                monitor.runningProperty());

        /*modelProperty().addListener(this::onModelChange);
        viewProperty().addListener(this::onViewChange);*/
        bind(model, view);
    }

    protected void bind(EonTimerModel model, EonTimerView view) {
        // TODO: refactor
        /*model.getEonTimerSettingsModel().getActionSettingsModel().modeProperty().addListener(actionModeChangeListener);
        monitor.refreshIntervalProperty().bind(model.getEonTimerSettingsModel().getTimerSettingsModel().refreshIntervalProperty());
        actionHandler.intervalProperty().bind(model.getEonTimerSettingsModel().getActionSettingsModel().intervalProperty());
        actionHandler.countProperty().bind(model.getEonTimerSettingsModel().getActionSettingsModel().countProperty());
        visualAction.activeFillProperty().bind(model.getEonTimerSettingsModel().getActionSettingsModel().colorProperty());
        soundAction.soundProperty().bind(model.getEonTimerSettingsModel().getActionSettingsModel().soundProperty());

        view.tabDisableProperty().bind(monitor.runningProperty());
        view.currentStageDisplayLblBackgroundColorProperty().bind(visualAction.fillProperty());
        view.currentStageDisplayTextProperty().bind(displayHandler.currentStageProperty());
        view.minutesBeforeTargetValueTextProperty().bind(displayHandler.minutesBeforeTargetProperty());
        view.nextStageDisplayTextProperty().bind(displayHandler.nextStageProperty());

        view.timerTypeProperty().addListener(timerTypeChangeListener);
        view.timerBtnTextProperty().bind(timerBtnTextBinding);
        view.settingsBtnDisableProperty().bind(monitor.runningProperty());
        view.setSettingsBtnOnAction(this::onSettingsBtnAction);

        view.styleProperty().bind(model.getEonTimerSettingsModel().getThemeSettingsModel().toCssBinding());

        // region TODO: add variable target
        // view.updateBtnDisableProperty().bind(monitor.runningProperty()
        //        .and(isVariableTargetTimer.and(isTargetFrameSet.not()).not()));
        // endregion

        view.updateBtnDisableProperty().bind(monitor.runningProperty());
        view.setUpdateBtnOnAction(this::onUpdateBtnAction);
        view.setTimerBtnOnAction(this::onTimerBtnAction);

        refreshActionMode();
        refreshTimerType();*/
    }

    protected void unbind(EonTimerModel model, EonTimerView view) {
        // TODO: refactor
        /*model.getEonTimerSettingsModel().getActionSettingsModel().modeProperty().removeListener(actionModeChangeListener);
        monitor.refreshIntervalProperty().unbind();
        actionHandler.intervalProperty().unbind();
        actionHandler.countProperty().unbind();
        visualAction.activeFillProperty().unbind();
        soundAction.soundProperty().unbind();

        view.tabDisableProperty().unbind();
        view.currentStageDisplayLblBackgroundColorProperty().unbind();
        view.currentStageDisplayTextProperty().unbind();
        view.minutesBeforeTargetValueTextProperty().unbind();
        view.nextStageDisplayTextProperty().unbind();

        view.timerTypeProperty().removeListener(timerTypeChangeListener);
        view.timerBtnTextProperty().unbind();
        view.settingsBtnDisableProperty().unbind();
        view.setSettingsBtnOnAction(null);
        view.updateBtnDisableProperty().unbind();
        view.setUpdateBtnOnAction(null);
        view.setTimerBtnOnAction(null);*/
    }

    public void onModelChange(ObservableValue<? extends EonTimerModel> observable, EonTimerModel oldModel, EonTimerModel newModel) {
        /*super.onModelChange(observable, oldModel, newModel);
        gen5TimerController.setModel(newModel.getGen5TimerModel());
        gen4TimerController.setModel(newModel.getGen4TimerModel());
        gen3TimerController.setModel(newModel.getGen3TimerModel());
        customTimerController.setModel(newModel.getCustomTimerModel());
        eonTimerSettingsController.setModel(newModel.getEonTimerSettingsModel());*/
    }

    public void onViewChange(ObservableValue<? extends EonTimerView> observable, EonTimerView oldView, EonTimerView newView) {
        /*super.onViewChange(observable, oldView, newView);
        gen5TimerController.setView(newView.getGen5TimerView());
        gen4TimerController.setView(newView.getGen4TimerView());
        gen3TimerController.setView(newView.getGen3TimerView());
        customTimerController.setView(newView.getCustomTimerView());
        eonTimerSettingsController.setView(newView.getEonTimerSettingsView());*/
    }

    private String computeTimerBtnText() {
        ResourceBundle bundle = ResourceBundles.getBundle(EonTimerView.class);
        String start = bundle.getString("EonTimerView.timerBtn.text.start");
        String stop = bundle.getString("EonTimerView.timerBtn.text.stop");
        return monitor.isRunning() ? stop : start;
    }

    private void onSettingsBtnAction(ActionEvent e) {
        Dialog dialog = new Dialog();
        ResourceBundle bundle = ResourceBundles.getBundle(EonTimerView.class);
        dialog.setTitle(bundle.getString("EonTimerView.eonTimerSettingsView.title"));
//        dialog.setDialogPane(getView().getEonTimerSettingsView());
        dialog.showAndWait();
    }

    private void onUpdateBtnAction(ActionEvent e) {
        /*switch (getView().getTimerType()) {
            case GEN5:
                gen5TimerController.calibrate();
                break;
            case GEN4:
                gen4TimerController.calibrate();
                break;
            case GEN3:
                gen3TimerController.calibrate();
                break;
        }*/
    }

    private void onTimerBtnAction(ActionEvent e) {
        if (monitor.isRunning())
            monitor.cancel();
        else
            monitor.run();
    }

    private void onActionModeChange(ObservableValue<? extends ActionMode> observable, ActionMode oldMode, ActionMode newMode) {
        actionHandler.getActions().clear();
        if (newMode == ActionMode.AUDIO || newMode == ActionMode.AV)
            actionHandler.getActions().add(soundAction);
        if (newMode == ActionMode.VISUAL || newMode == ActionMode.AV)
            actionHandler.getActions().add(visualAction);
    }

    private void refreshActionMode() {
        /*ActionSettingsModel actionSettingsModel = getModel().getEonTimerSettingsModel().getActionSettingsModel();
        onActionModeChange(actionSettingsModel.modeProperty(), null, actionSettingsModel.getMode());*/
    }

    private void onTimerTypeChange(ObservableValue<? extends EonTimerView.TimerType> observable, EonTimerView.TimerType oldType, EonTimerView.TimerType newType) {
        monitor.timerProperty().unbind();
        switch (newType) {
            case GEN5:
                monitor.timerProperty().bind(gen5TimerController.timerProperty());
                break;
            case GEN4:
                monitor.timerProperty().bind(gen4TimerController.timerProperty());
                break;
            case GEN3:
                monitor.timerProperty().bind(gen3TimerController.timerProperty());
                break;
            case CUSTOM:
                monitor.timerProperty().bind(customTimerController.timerProperty());
                break;
        }
    }

    private void refreshTimerType() {
//        onTimerTypeChange(getView().timerTypeProperty(), null, getView().getTimerType());
    }
}
