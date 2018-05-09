package com.github.dylmeadows.common.javafx.beans;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

class ChangeListenerHandle<T> implements ListenerHandle {

    private ObservableValue<T> observable;

    private ChangeListener<T> listener;

    ChangeListenerHandle(ObservableValue<T> observable, ChangeListener<T> listener) {
        this.observable = observable;
        this.listener = listener;
    }

    @Override
    public void attach() {
        observable.addListener(listener);
    }

    @Override
    public void detach() {
        observable.removeListener(listener);
    }
}
