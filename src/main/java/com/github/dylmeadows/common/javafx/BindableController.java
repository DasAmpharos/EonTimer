package com.github.dylmeadows.common.javafx;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;

public abstract class BindableController<M extends Model, V extends Parent> extends Controller<M, V> implements
        ModelChangeListener<M>,
        ViewChangeListener<V> {

    protected BindableController(M model, V view, boolean bind) {
        super(model, view);
        modelProperty().addListener(this::onModelChange);
        viewProperty().addListener(this::onViewChange);
        if (bind) bind(model, view);
    }

    @Override
    public void onModelChange(ObservableValue<? extends M> observable, M oldModel, M newModel) {
        unbind(oldModel, getView());
        bind(newModel, getView());
    }

    @Override
    public void onViewChange(ObservableValue<? extends V> observable, V oldView, V newView) {
        unbind(getModel(), oldView);
        bind(getModel(), newView);
    }

    protected abstract void bind(M model, V view);

    protected abstract void unbind(M model, V view);
}
