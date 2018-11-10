package com.github.dylmeadows.eontimer.controller;

import com.github.dylmeadows.eontimer.core.TimerState;
import com.github.dylmeadows.eontimer.model.Stage;
import com.github.dylmeadows.eontimer.model.timer.TimerConstants;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.rxjavafx.sources.Change;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import moe.tristan.easyfxml.api.FxmlController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimerDisplayController implements FxmlController {

    @FXML
    private Label currentStageLbl;
    @FXML
    private Label minutesBeforeTargetLbl;
    @FXML
    private Label nextStageLbl;

    private final TimerState timerState;

    private static final String NULL_TIME_SPAN = "0:00";
    private static final String INFINITE_TIME_SPAN = "?:??";

    @Autowired
    public TimerDisplayController(final TimerState timerState) {
        this.timerState = timerState;
    }

    @Override
    public void initialize() {
        JavaFxObservable.changesOf(timerState.remainingProperty())
            .subscribeOn(JavaFxScheduler.platform())
            .map(Change::getNewVal)
            .map(Number::longValue)
            .map(this::formatTime)
            .doOnNext(currentStageLbl::setText)
            .subscribe();
        JavaFxObservable.changesOf(timerState.nextStageProperty())
            .subscribeOn(JavaFxScheduler.platform())
            .map(Change::getNewVal)
            .map(Stage::getLength)
            .map(this::formatTime)
            .doOnNext(nextStageLbl::setText)
            .subscribe();
    }

    private String formatTime(long duration) {
        return duration == TimerConstants.NULL_TIME_SPAN ? NULL_TIME_SPAN
            : duration == TimerConstants.INFINITE_TIME_SPAN ? INFINITE_TIME_SPAN
            : String.format("%d:%02d", duration / 1000, (duration / 10) % 100);
    }
}
