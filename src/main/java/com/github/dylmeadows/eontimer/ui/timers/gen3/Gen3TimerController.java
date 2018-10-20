package com.github.dylmeadows.eontimer.ui.timers.gen3;

import com.github.dylmeadows.eontimer.model.Gen3TimerMode;
import com.github.dylmeadows.eontimer.model.timers.NullTimer;
import com.github.dylmeadows.eontimer.model.timers.Timer;
import com.github.dylmeadows.eontimer.model.timers.VariableTargetFrameTimer;
import com.github.dylmeadows.eontimer.ui.timers.TimerController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

public class Gen3TimerController extends TimerController<Gen3TimerModel, Gen3TimerView> {

    public Gen3TimerController(Gen3TimerModel model, Gen3TimerView view) {
        super(model, view);
    }

    public Timer createTimer() {
        Gen3TimerModel model = /*getModel();*/ null;
        switch (model.getMode()) {
            // TODO: refactor
            /*case STANDARD: {
                return new FrameTimer(
                        model.getCalibration(),
                        model.getPreTimer(),
                        model.getTargetFrame(),
                        model.getConsole());
            }
            case VARIABLE_TARGET: {
                return new VariableTargetFrameTimer(
                        model.getConsole());
            }*/
            default:
                return new NullTimer();
        }
    }

    @Override
    protected ObjectBinding<Timer> createTimerBinding(Gen3TimerModel model) {
        return Bindings.createObjectBinding(this::createTimer,
                model.modeProperty(), model.calibrationProperty(),
                model.preTimerProperty(), model.targetFrameProperty(),
                model.consoleProperty());
    }

    @Override
    protected void bind(Gen3TimerModel model, Gen3TimerView view) {
        super.bind(model, view);
        view.modeProperty().bindBidirectional(model.modeProperty());
        view.calibrationProperty().bindBidirectional(model.calibrationProperty());
        view.preTimerProperty().bindBidirectional(model.preTimerProperty());
        view.targetFrameProperty().bindBidirectional(model.targetFrameProperty());
        view.frameHitProperty().bindBidirectional(model.frameHitProperty());

        view.modeFieldDisableProperty().bind(allFieldsDisableProperty());
        view.calibrationFieldDisableProperty().bind(allFieldsDisableProperty());
        view.preTimerFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetFrameFieldDisableProperty().bind(allFieldsDisableProperty()
                .and(view.modeProperty().isNotEqualTo(Gen3TimerMode.VARIABLE_TARGET)));
        view.frameHitFieldDisableProperty().bind(allFieldsDisableProperty());
    }

    @Override
    protected void unbind(Gen3TimerModel model, Gen3TimerView view) {
        super.unbind(model, view);
        view.modeProperty().unbindBidirectional(model.modeProperty());
        view.calibrationProperty().unbindBidirectional(model.calibrationProperty());
        view.preTimerProperty().unbindBidirectional(model.preTimerProperty());
        view.targetFrameProperty().unbindBidirectional(model.targetFrameProperty());
        view.frameHitProperty().unbindBidirectional(model.frameHitProperty());

        view.modeFieldDisableProperty().unbind();
        view.calibrationFieldDisableProperty().unbind();
        view.preTimerFieldDisableProperty().unbind();
        view.targetFrameFieldDisableProperty().unbind();
        view.frameHitFieldDisableProperty().unbind();
    }

    @Override
    public void calibrate() {
        /*switch (getModel().getMode()) {
            case STANDARD: {
                // TODO: refactor
                *//*int temp = FrameTimer.calibrate((FrameTimer) getTimer(), getView().getFrameHit());
                int calibration = getView().getCalibration() + temp;
                getView().setCalibration(calibration);*//*
                break;
            }
            case VARIABLE_TARGET: {
                ((VariableTargetFrameTimer) getTimer()).setTargetFrame(
                        getView().getTargetFrame());
                break;
            }
        }
        getView().setFrameHitText("");*/
    }
}
