package com.github.dylmeadows.common.javafx.beans;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public interface ListenerHandle {

    static <T> ListenerHandle createHandle(ObservableValue<T> observable, ChangeListener<T> listener) {
        return new ChangeListenerHandle<>(observable, listener);
    }

    static ListenerHandle createHandle(Observable observable, InvalidationListener listener) {
        return new InvalidationListenerHandle(observable, listener);
    }

    static ListenerHandle createHandle(ListenerHandle... handles) {
        return new MultiListenerHandle(handles);
    }

    void attach();

    void detach();
}
