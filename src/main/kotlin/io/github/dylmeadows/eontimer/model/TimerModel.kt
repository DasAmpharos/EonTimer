package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerModel {

    val stagesProperty: ObjectProperty<List<Duration>> = SimpleObjectProperty(ArrayList())
    var stages: List<Duration> by stagesProperty

    val runningProperty: BooleanProperty = SimpleBooleanProperty(false)
    var running by runningProperty
}

open class StageModel {
    val totalDurationProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var totalDuration by totalDurationProperty

    val remainingProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var remaining by remainingProperty
}