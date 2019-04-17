package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerModel {
    val stagesProperty: ObjectProperty<List<Duration>> = SimpleObjectProperty(ArrayList())
    var stages: List<Duration> by stagesProperty
}