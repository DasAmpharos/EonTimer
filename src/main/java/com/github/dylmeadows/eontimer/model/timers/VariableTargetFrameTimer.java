package com.github.dylmeadows.eontimer.model.timers;

import com.github.dylmeadows.eontimer.util.Calibrations;
import com.github.dylmeadows.eontimer.model.Console;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Duration;

public class VariableTargetFrameTimer implements VariableTimer, Timer {

    private final IntegerProperty targetFrame;

    private final ObjectProperty<Console> console;

    public VariableTargetFrameTimer(Console console) {
        this.console = new SimpleObjectProperty<>(console);
        this.targetFrame = new SimpleIntegerProperty(-1);
        this.targetFrame.addListener((observable, oldValue, newValue) -> initialize());
        initialize();
    }

    private Duration calculateTarget() {
        if (getTargetFrame() == -1)
            // TODO: refactor
            /*return TimerConstants.INFINITE_TIME_SPAN;*/
            return Duration.ZERO;
        else
            return Duration.ofMillis(Calibrations.convertToMillis(getTargetFrame(), getConsole()));
    }

    protected void initialize() {
        int i = 0;
        Duration stage;
        // TODO: refactor
        /*while ((stage = getStage(i)) != TimerConstants.NULL_TIME_SPAN) {
            getStages().add(stage);
            i++;
        }*/
    }

    @Override
    public void reset() {
        setTargetFrame(-1);
        initialize();
    }

    public int getMinutesBeforeTarget() {
        return 0;
    }

    private Duration getStage(int stage) {
        switch (stage) {
            case 0:
                return calculateTarget();
            default:
                // TODO: refactor
                /*return TimerConstants.NULL_TIME_SPAN;*/
                return Duration.ZERO;
        }
    }

    public int getTargetFrame() {
        return targetFrame.get();
    }

    public IntegerProperty targetFrameProperty() {
        return targetFrame;
    }

    public void setTargetFrame(int targetFrame) {
        this.targetFrame.set(targetFrame);
    }

    public Console getConsole() {
        return console.get();
    }

    public ObjectProperty<Console> consoleProperty() {
        return console;
    }

    public void setConsole(Console console) {
        this.console.set(console);
    }
}
