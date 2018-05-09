package com.github.dylmeadows.common.javafx;

import javafx.beans.value.ObservableValue;

public interface ModelChangeListener<T extends Model> {

    void onModelChange(ObservableValue<? extends T> observable, T oldModel, T newModel);
}
