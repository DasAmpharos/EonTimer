package com.github.dylmeadows.common.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;

public abstract class Controller<M extends Model, V extends Parent> {

    private final ObjectProperty<M> model;

    private final ObjectProperty<V> view;

    protected Controller(M model, V view) {
        this.model = new SimpleObjectProperty<>(model);
        this.view = new SimpleObjectProperty<>(view);
    }

    public final M getModel() {
        return model.get();
    }

    protected final ObjectProperty<M> modelProperty() {
        return model;
    }

    public final void setModel(M model) {
        this.model.set(model);
    }

    public final V getView() {
        return view.get();
    }

    protected final ObjectProperty<V> viewProperty() {
        return view;
    }

    public final void setView(V view) {
        this.view.set(view);
    }
}
