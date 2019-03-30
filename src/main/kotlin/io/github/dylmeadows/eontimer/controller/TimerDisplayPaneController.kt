package io.github.dylmeadows.eontimer.controller

import io.github.dylmeadows.common.javafx.scene.paint.Colors
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.action.TimerActionService
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.isActive
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerDisplayPaneController @Autowired constructor(
    private val timerState: TimerState,
    private val timerActionService: TimerActionService,
    private val actionSettingsModel: ActionSettingsModel) {

    @FXML
    private lateinit var currentStageLbl: Label
    @FXML
    private lateinit var minutesBeforeTargetLbl: Label
    @FXML
    private lateinit var nextStageLbl: Label

    fun initialize() {
        timerState.currentStageProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(Number::toLong)
            .map(this::formatTime)
            .subscribe(currentStageLbl::setText)
        timerState.remainingProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(Number::toLong)
            .map(this::formatTime)
            .subscribe(currentStageLbl::setText)
        timerState.minutesBeforeTargetProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(Number::toString)
            .subscribe(minutesBeforeTargetLbl::setText)
        timerState.nextStageProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map(Number::toLong)
            .map(this::formatTime)
            .subscribe(nextStageLbl::setText)
        timerActionService.activeProperty.asFlux()
            .subscribe { currentStageLbl.isActive = it }

        actionSettingsModel.colorProperty.asFlux()
            .map { Colors.toHex(it) }
            .map { "-theme-active: $it"}
            .subscribe(currentStageLbl::setStyle)
    }

    private fun formatTime(duration: Long): String {
        return when (duration) {
            TimerConstants.NULL_TIME_SPAN -> "0:00"
            TimerConstants.INFINITE_TIME_SPAN -> "?:??"
            else -> String.format("%d:%02d", duration / 1000, duration / 10 % 100)
        }
    }
}