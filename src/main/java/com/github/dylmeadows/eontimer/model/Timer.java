package com.github.dylmeadows.eontimer.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class Timer {

    private final ObjectProperty<List<Stage>> stages = new SimpleObjectProperty<>(Collections.emptyList());

    public List<Stage> getStages() {
        return stages.get();
    }

    public ObjectProperty<List<Stage>> stagesProperty() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        Objects.requireNonNull(stages);
        this.stages.set(Collections.unmodifiableList(stages));
    }
}
