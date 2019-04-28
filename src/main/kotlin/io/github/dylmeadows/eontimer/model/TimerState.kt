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
class TimerState {
    val totalTimeProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var totalTime: Duration by totalTimeProperty

    val totalElapsedProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var totalElapsed: Duration by totalElapsedProperty

    val currentStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var currentStage: Duration by currentStageProperty

    val nextStageProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var nextStage: Duration by nextStageProperty

    val currentRemainingProperty: ObjectProperty<Duration> = SimpleObjectProperty(Duration.ZERO)
    var currentRemaining: Duration by currentRemainingProperty

    val runningProperty: BooleanProperty = SimpleBooleanProperty(false)
    var running: Boolean by runningProperty
}
