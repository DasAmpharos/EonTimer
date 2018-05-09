package com.github.dylmeadows.eontimer.ui.timers.gen4;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.HasTimer;
import com.github.dylmeadows.eontimer.TimerFactory;
import com.github.dylmeadows.eontimer.timers.DelayTimer;
import com.github.dylmeadows.eontimer.timers.Timer;
import com.github.dylmeadows.eontimer.timers.NullTimer;
import com.github.dylmeadows.eontimer.ui.timers.TimerController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

public class Gen4TimerController extends TimerController<Gen4TimerModel, Gen4TimerView> implements HasTimer, TimerFactory {

    public Gen4TimerController(Gen4TimerModel model, Gen4TimerView view) {
        super(model, view);
    }

    @Override
    public Timer createTimer() {
        Gen4TimerModel model = getModel();
        switch (model.getMode()) {
            case STANDARD:
                return new DelayTimer(
                        model.getCalculatedCalibration(),
                        model.getTargetDelay(),
                        model.getTargetSecond(),
                        model.getConsole(),
                        model.getMinimumLength());
            default:
                return new NullTimer();
        }
    }

    @Override
    protected ObjectBinding<Timer> createTimerBinding(Gen4TimerModel model) {
        return Bindings.createObjectBinding(this::createTimer,
                model.modeProperty(), model.calculatedCalibrationProperty(), model.calibratedSecondProperty(),
                model.targetDelayProperty(), model.targetSecondProperty(),
                model.consoleProperty(), model.minimumLengthProperty());
    }

    @Override
    protected void bind(Gen4TimerModel model, Gen4TimerView view) {
        super.bind(model, view);
        view.modeProperty().bindBidirectional(model.modeProperty());
        view.calibratedDelayProperty().bindBidirectional(model.calibratedDelayProperty());
        view.calibratedSecondProperty().bindBidirectional(model.calibratedSecondProperty());
        view.targetDelayProperty().bindBidirectional(model.targetDelayProperty());
        view.targetSecondProperty().bindBidirectional(model.targetSecondProperty());
        view.delayHitProperty().bindBidirectional(model.delayHitProperty());

        view.modeFieldDisableProperty().bind(allFieldsDisableProperty());
        view.calibratedDelayFieldDisableProperty().bind(allFieldsDisableProperty());
        view.calibratedSecondFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetDelayFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetSecondFieldDisableProperty().bind(allFieldsDisableProperty());
        view.delayHitFieldDisableProperty().bind(allFieldsDisableProperty());
    }

    @Override
    protected void unbind(Gen4TimerModel model, Gen4TimerView view) {
        super.unbind(model, view);
        view.modeProperty().unbindBidirectional(model.modeProperty());
        view.calibratedDelayProperty().unbindBidirectional(model.calibratedDelayProperty());
        view.calibratedSecondProperty().unbindBidirectional(model.calibratedSecondProperty());
        view.targetDelayProperty().unbindBidirectional(model.targetDelayProperty());
        view.targetSecondProperty().unbindBidirectional(model.targetSecondProperty());
        view.delayHitProperty().unbindBidirectional(model.delayHitProperty());

        view.modeFieldDisableProperty().unbind();
        view.calibratedDelayFieldDisableProperty().unbind();
        view.calibratedSecondFieldDisableProperty().unbind();
        view.targetDelayFieldDisableProperty().unbind();
        view.targetSecondFieldDisableProperty().unbind();
        view.delayHitFieldDisableProperty().unbind();
    }

    @Override
    public void calibrate() {
        switch (getModel().getMode()) {
            case STANDARD: {
                int temp = DelayTimer.calibrate((DelayTimer) getTimer(), getView().getDelayHit());
                int calibration = getView().getCalibratedDelay() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setCalibratedDelay(calibration);
                break;
            }
        }
        getView().setDelayHitText("");
    }
}
