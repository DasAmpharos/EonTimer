package io.github.dylmeadows.eontimer.controller

import io.github.dylmeadows.common.javafx.scene.paint.Colors
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.service.action.TimerActionService
import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.anyChangesOf
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.isActive
import io.github.dylmeadows.eontimer.util.toSeconds
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class TimerDisplayPane @Autowired constructor(
    private val timerState: TimerState,
    private val timerActionService: TimerActionService,
    private val actionSettingsModel: ActionSettingsModel) {

    @FXML
    lateinit var currentStageLbl: Label
    @FXML
    lateinit var minutesBeforeTargetLbl: Label
    @FXML
    lateinit var nextStageLbl: Label

    fun initialize() {
        timerState.currentStageProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(this::formatTime)
            .subscribe(currentStageLbl::setText)
        timerState.currentRemainingProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(this::formatTime)
            .subscribe(currentStageLbl::setText)
        anyChangesOf(
            timerState.totalTimeProperty,
            timerState.totalElapsedProperty)
            .subscribeOn(JavaFxScheduler.platform())
            .subscribe {
                minutesBeforeTargetLbl.text =
                    formatMinutesBeforeTarget(it.t1, it.t2)
            }
        timerState.nextStageProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(this::formatTime)
            .subscribe(nextStageLbl::setText)
        timerActionService.activeProperty.asFlux()
            .subscribe { currentStageLbl.isActive = it }

        actionSettingsModel.colorProperty.asFlux()
            .map { Colors.toHex(it) }
            .map { "-theme-active: $it" }
            .subscribe(currentStageLbl::setStyle)
    }

    private fun formatMinutesBeforeTarget(totalTime: Duration, totalElapsed: Duration): String {
        return when (totalTime) {
            INDEFINITE -> "?"
            else -> {
                val remaining = totalTime - totalElapsed
                remaining.toMinutes().toString()
            }
        }
    }

    private fun formatTime(duration: Duration): String {
        return when (duration) {
            INDEFINITE -> "?:??"
            else -> String.format("%d:%02d",
                duration.toSeconds(),
                duration.toMillis()
                    / 10 % 100)
        }
    }
}