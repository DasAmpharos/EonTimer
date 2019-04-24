package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.TimerRunnerService
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.asLongField
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.showWhen
import io.github.dylmeadows.eontimer.util.sum
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerPane @Autowired constructor(
    private val model: Gen3TimerModel,
    private val timerState: TimerState,
    private val timerRunnerService: TimerRunnerService,
    private val calibrationService: CalibrationService) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen3TimerMode>
    @FXML
    private lateinit var calibrationField: TextField
    @FXML
    private lateinit var preTimerField: TextField
    @FXML
    private lateinit var targetFrameField: TextField
    @FXML
    private lateinit var setTargetFrameBtn: Button
    @FXML
    private lateinit var frameHitField: TextField

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)

        calibrationField.asLongField().valueProperty
            .bindBidirectional(model.calibrationProperty)

        preTimerField.asLongField().valueProperty
            .bindBidirectional(model.preTimerProperty)

        this.targetFrameField.asLongField().valueProperty
            .bindBidirectional(model.targetFrameProperty)

        setTargetFrameBtn.showWhen(model.modeProperty
            .isEqualTo(Gen3TimerMode.VARIABLE_TARGET))
        setTargetFrameBtn.setOnAction {
            if (timerState.running) {
                val duration = calibrationService.toMillis(model.targetFrame)
                timerRunnerService.stages[1] = duration.milliseconds
                timerState.totalTime = timerRunnerService.stages.sum()
                setTargetFrameBtn.isDisable = true
            }
        }

        frameHitField.asLongField().valueProperty
            .bindBidirectional(model.frameHitProperty)
        frameHitField.text = ""
    }
}