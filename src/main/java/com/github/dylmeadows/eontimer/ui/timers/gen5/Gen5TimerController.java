package com.github.dylmeadows.eontimer.ui.timers.gen5;

import com.github.dylmeadows.eontimer.CalibrationHelper;
import com.github.dylmeadows.eontimer.timers.*;
import com.github.dylmeadows.eontimer.ui.timers.TimerController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

public class Gen5TimerController extends TimerController<Gen5TimerModel, Gen5TimerView> {

    public Gen5TimerController(Gen5TimerModel model, Gen5TimerView view) {
        super(model, view);
    }

    @Override
    public Timer createTimer() {
        Gen5TimerModel model = getModel();
        switch (model.getMode()) {
            case STANDARD:
                return new SimpleTimer(
                        model.getCalculatedCalibration(),
                        model.getTargetSecond(),
                        model.getConsole(),
                        model.getMinimumLength());
            case C_GEAR:
                return new DelayTimer(
                        model.getCalculatedCalibration(),
                        model.getTargetDelay(),
                        model.getTargetSecond(),
                        model.getConsole(),
                        model.getMinimumLength());
            case ENTRALINK:
                return new EntralinkTimer(
                        model.getCalculatedCalibration(),
                        model.getCalculatedEntralinkCalibration(),
                        model.getTargetDelay(),
                        model.getTargetSecond(),
                        model.getConsole(),
                        model.getMinimumLength());
            case ENTRALINK_PLUS:
                return new EnhancedEntralinkTimer(
                        model.getCalculatedCalibration(),
                        model.getCalculatedEntralinkCalibration(),
                        model.getTargetDelay(),
                        model.getTargetSecond(),
                        model.getTargetAdvances(),
                        model.getFrameCalibration(),
                        model.getConsole(),
                        model.getMinimumLength());
            default:
                return new NullTimer();
        }
    }

    @Override
    protected ObjectBinding<Timer> createTimerBinding(Gen5TimerModel model) {
        return Bindings.createObjectBinding(this::createTimer,
                model.modeProperty(), model.calculatedCalibrationProperty(),
                model.targetDelayProperty(), model.targetSecondProperty(),
                model.calculatedCalibrationProperty(),
                model.frameCalibrationProperty(),
                model.targetAdvancesProperty(),
                model.consoleProperty(),
                model.minimumLengthProperty());
    }

    @Override
    protected void bind(Gen5TimerModel model, Gen5TimerView view) {
        super.bind(model, view);
        view.modeProperty().bindBidirectional(model.modeProperty());
        view.calibrationProperty().bindBidirectional(model.calibrationProperty());
        view.targetDelayProperty().bindBidirectional(model.targetDelayProperty());
        view.targetSecondProperty().bindBidirectional(model.targetSecondProperty());
        view.entralinkCalibrationProperty().bindBidirectional(model.entralinkCalibrationProperty());
        view.frameCalibrationProperty().bindBidirectional(model.frameCalibrationProperty());
        view.targetAdvancesProperty().bindBidirectional(model.targetAdvancesProperty());
        view.secondHitProperty().bindBidirectional(model.secondHitProperty());
        view.delayHitProperty().bindBidirectional(model.delayHitProperty());
        view.actualAdvancesProperty().bindBidirectional(model.actualAdvancesProperty());

        view.modeFieldDisableProperty().bind(allFieldsDisableProperty());
        view.calibrationFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetDelayFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetSecondFieldDisableProperty().bind(allFieldsDisableProperty());
        view.entralinkCalibrationFieldDisableProperty().bind(allFieldsDisableProperty());
        view.frameCalibrationFieldDisableProperty().bind(allFieldsDisableProperty());
        view.targetAdvancesFieldDisableProperty().bind(allFieldsDisableProperty());
        view.secondHitFieldDisableProperty().bind(allFieldsDisableProperty());
        view.delayHitFieldDisableProperty().bind(allFieldsDisableProperty());
        view.actualAdvancesFieldDisableProperty().bind(allFieldsDisableProperty());
    }

    @Override
    protected void unbind(Gen5TimerModel model, Gen5TimerView view) {
        super.unbind(model, view);
        view.modeProperty().unbindBidirectional(model.modeProperty());
        view.calibrationProperty().unbindBidirectional(model.calibrationProperty());
        view.targetDelayProperty().unbindBidirectional(model.targetDelayProperty());
        view.targetSecondProperty().unbindBidirectional(model.targetSecondProperty());
        view.entralinkCalibrationProperty().unbindBidirectional(model.entralinkCalibrationProperty());
        view.frameCalibrationProperty().unbindBidirectional(model.frameCalibrationProperty());
        view.targetAdvancesProperty().unbindBidirectional(model.targetAdvancesProperty());
        view.secondHitProperty().unbindBidirectional(model.secondHitProperty());
        view.delayHitProperty().unbindBidirectional(model.delayHitProperty());
        view.actualAdvancesProperty().unbindBidirectional(model.actualAdvancesProperty());

        view.modeFieldDisableProperty().unbind();
        view.calibrationFieldDisableProperty().unbind();
        view.targetDelayFieldDisableProperty().unbind();
        view.targetSecondFieldDisableProperty().unbind();
        view.entralinkCalibrationFieldDisableProperty().unbind();
        view.frameCalibrationFieldDisableProperty().unbind();
        view.targetAdvancesFieldDisableProperty().unbind();
        view.secondHitFieldDisableProperty().unbind();
        view.delayHitFieldDisableProperty().unbind();
        view.actualAdvancesFieldDisableProperty().unbind();
    }

    @Override
    public void calibrate() {
        switch (getModel().getMode()) {
            case STANDARD: {
                int temp = SimpleTimer.calibrate((SimpleTimer) getTimer(), getView().getSecondHit());
                int calibration = getView().getCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setCalibration(calibration);
                break;
            }
            case C_GEAR: {
                int temp = DelayTimer.calibrate((DelayTimer) getTimer(), getView().getDelayHit());
                int calibration = getView().getCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setCalibration(calibration);
                break;
            }
            case ENTRALINK: {
                int temp = SimpleTimer.calibrate((SimpleTimer) getTimer(), getView().getSecondHit());
                int calibration = getView().getCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setCalibration(calibration);

                temp = EntralinkTimer.calibrate((DelayTimer) getTimer(), getView().getDelayHit() - temp);
                calibration = getView().getEntralinkCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setEntralinkCalibration(calibration);
                break;
            }
            case ENTRALINK_PLUS: {
                int temp = SimpleTimer.calibrate((SimpleTimer) getTimer(), getView().getSecondHit());
                int calibration = getView().getCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setCalibration(calibration);

                temp = EntralinkTimer.calibrate((DelayTimer) getTimer(), getView().getDelayHit() - temp);
                calibration = getView().getEntralinkCalibration() + (getModel().isPrecisionCalibrationMode() ? temp :
                        CalibrationHelper.convertToDelays(temp, getModel().getConsole()));
                getView().setEntralinkCalibration(calibration);

                temp = EnhancedEntralinkTimer.calibrate((EnhancedEntralinkTimer) getTimer(), getView().getActualAdvances());
                calibration = getView().getFrameCalibration() + temp;
                getView().setFrameCalibration(calibration);
                break;
            }
        }
        getView().setSecondHitText("");
        getView().setDelayHitText("");
        getView().setActualAdvancesText("");
    }
}
