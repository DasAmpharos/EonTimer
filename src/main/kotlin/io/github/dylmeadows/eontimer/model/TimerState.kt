package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.*
import org.springframework.stereotype.Component

@Component
class TimerState {
    val remainingProperty: LongProperty = SimpleLongProperty()
    var remaining by remainingProperty

    val currentStageProperty: LongProperty = SimpleLongProperty()
    var currentStage by currentStageProperty

    val minutesBeforeTargetProperty: LongProperty = SimpleLongProperty()
    var minutesBeforeTarget by minutesBeforeTargetProperty

    val nextStageProperty: LongProperty = SimpleLongProperty()
    var nextStage by nextStageProperty

    val runningProperty: BooleanProperty = SimpleBooleanProperty()
    var running by runningProperty
}