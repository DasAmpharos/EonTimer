package com.github.dylmeadows.common.javafx;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;

public interface ViewChangeListener<T extends Parent> {
    void onViewChange(ObservableValue<? extends T> observable, T oldView, T newView);
}
