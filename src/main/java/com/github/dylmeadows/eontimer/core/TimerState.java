package com.github.dylmeadows.eontimer.core;

import com.github.dylmeadows.eontimer.model.Stage;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@SuppressWarnings({"unused", "WeakerAccess"})
public class TimerState {

    private final ReadOnlyLongWrapper remaining = new ReadOnlyLongWrapper();
    private final ReadOnlyObjectWrapper<Stage> currentStage = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<Stage> nextStage = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyIntegerWrapper minutesBeforeTarget = new ReadOnlyIntegerWrapper();
    private final ReadOnlyBooleanWrapper running = new ReadOnlyBooleanWrapper();

    public long getRemaining() {
        return remaining.get();
    }

    public ReadOnlyLongProperty remainingProperty() {
        return remaining.getReadOnlyProperty();
    }

    void setRemaining(long remaining) {
        this.remaining.set(remaining);
    }

    public Stage getCurrentStage() {
        return currentStage.get();
    }

    public ReadOnlyObjectProperty<Stage> currentStageProperty() {
        return currentStage.getReadOnlyProperty();
    }

    void setCurrentStage(Stage currentStage) {
        Objects.requireNonNull(currentStage);
        this.currentStage.set(currentStage);
    }

    public Stage getNextStage() {
        return nextStage.get();
    }

    public ReadOnlyObjectProperty<Stage> nextStageProperty() {
        return nextStage.getReadOnlyProperty();
    }

    void setNextStage(Stage nextStage) {
        Objects.requireNonNull(nextStage);
        this.nextStage.set(nextStage);
    }

    public int getMinutesBeforeTarget() {
        return minutesBeforeTarget.get();
    }

    public ReadOnlyIntegerProperty minutesBeforeTargetProperty() {
        return minutesBeforeTarget.getReadOnlyProperty();
    }

    void setMinutesBeforeTarget(int minutesBeforeTarget) {
        this.minutesBeforeTarget.set(minutesBeforeTarget);
    }

    public boolean isRunning() {
        return running.get();
    }

    public ReadOnlyBooleanProperty runningProperty() {
        return running.getReadOnlyProperty();
    }

    void setRunning(boolean running) {
        this.running.set(running);
    }
}
