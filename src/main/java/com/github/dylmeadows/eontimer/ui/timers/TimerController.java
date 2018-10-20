package com.github.dylmeadows.eontimer.ui.timers;

import com.github.dylmeadows.eontimer.model.timers.Timer;
import com.github.dylmeadows.eontimer.model.timers.NullTimer;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;

public abstract class TimerController<M extends TimerModel, V extends Parent> {

    private final BooleanProperty allFieldsDisable;

    private final ReadOnlyObjectWrapper<Timer> timer;

    protected TimerController(M model, V view) {
        this.allFieldsDisable = new SimpleBooleanProperty(false);
        this.timer = new ReadOnlyObjectWrapper<>(new NullTimer());
        bind(model, view);
    }

    protected void bind(M model, V view) {
        timer.bind(createTimerBinding(model));
    }

    protected void unbind(M model, V view) {
        timer.unbind();
    }

    public abstract void calibrate();

    protected abstract ObjectBinding<Timer> createTimerBinding(M model);

    public final Timer getTimer() {
        return timer.get();
    }

    public final ReadOnlyObjectProperty<Timer> timerProperty() {
        return timer.getReadOnlyProperty();
    }

    public boolean isAllFieldsDisable() {
        return allFieldsDisable.get();
    }

    public BooleanProperty allFieldsDisableProperty() {
        return allFieldsDisable;
    }

    public void setAllFieldsDisable(boolean allFieldsDisable) {
        this.allFieldsDisable.set(allFieldsDisable);
    }
}
