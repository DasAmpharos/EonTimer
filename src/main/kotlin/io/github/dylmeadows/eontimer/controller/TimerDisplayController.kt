package io.github.dylmeadows.eontimer.controller

import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.changesAsFlux
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerDisplayController @Autowired constructor(
    private val timerService: TimerService) {

    @FXML
    private lateinit var currentStageLbl: Label
    @FXML
    private lateinit var minutesBeforeTargetLbl: Label
    @FXML
    private lateinit var nextStageLbl: Label

    fun initialize() {
        timerService.stateProperty
            .changesAsFlux()
            .subscribeOn(JavaFxScheduler)
            .map { it.newValue }
            .subscribe {
                minutesBeforeTargetLbl.text = it.minutesBeforeTarget.toString()
                currentStageLbl.text = formatTime(it.remaining)
                nextStageLbl.text = formatTime(it.nextStage)
            }
    }

    private fun formatTime(duration: Long): String {
        return when (duration) {
            TimerConstants.NULL_TIME_SPAN -> "0:00"
            TimerConstants.INFINITE_TIME_SPAN -> "?:??"
            else -> String.format("%d:%02d", duration / 1000, duration / 10 % 100)
        }
    }
}