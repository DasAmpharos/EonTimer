package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.TimerRunnerService
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.spinner.LongValueFactory
import io.github.dylmeadows.eontimer.util.javafx.spinner.commitValue
import io.github.dylmeadows.eontimer.util.javafx.spinner.setOnFocusLost
import io.github.dylmeadows.eontimer.util.javafx.spinner.text
import io.github.dylmeadows.eontimer.util.javafx.spinner.valueProperty
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.setValue
import io.github.dylmeadows.eontimer.util.showWhen
import io.github.dylmeadows.eontimer.util.sum
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
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
    private lateinit var calibrationField: Spinner<Long>
    @FXML
    private lateinit var preTimerField: Spinner<Long>
    @FXML
    private lateinit var targetFrameField: Spinner<Long>
    @FXML
    private lateinit var setTargetFrameBtn: Button
    @FXML
    private lateinit var frameHitField: Spinner<Long>

    private val isPrimedProperty: BooleanProperty = SimpleBooleanProperty(true)
    private var isPrimed by isPrimedProperty

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
        calibrationField.valueProperty!!.bindBidirectional(model.calibrationProperty)
        calibrationField.parent.disableProperty().bind(timerState.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        preTimerField.valueFactory = LongValueFactory(0)
        preTimerField.valueProperty!!.bindBidirectional(model.preTimerProperty)
        preTimerField.parent.disableProperty().bind(timerState.runningProperty)
        preTimerField.setOnFocusLost(preTimerField::commitValue)

        targetFrameField.valueFactory = LongValueFactory(0)
        targetFrameField.valueProperty!!.bindBidirectional(model.targetFrameProperty)
        targetFrameField.parent.disableProperty().bind(
            model.modeProperty.isEqualTo(Gen3TimerMode.VARIABLE_TARGET)
                .and(timerState.runningProperty.not()
                    .or(isPrimedProperty.not()))
                .or(model.modeProperty.isEqualTo(Gen3TimerMode.STANDARD)
                    .and(timerState.runningProperty)))
        targetFrameField.setOnFocusLost(targetFrameField::commitValue)

        setTargetFrameBtn.showWhen(model.modeProperty
            .isEqualTo(Gen3TimerMode.VARIABLE_TARGET))
        setTargetFrameBtn.disableProperty().bind(isPrimedProperty.not())
        setTargetFrameBtn.setOnAction {
            if (timerState.running) {
                val duration = calibrationService.toMillis(model.targetFrame)
                timerRunnerService.stages[1] = duration.milliseconds
                timerState.totalTime = timerRunnerService.stages.sum()
                isPrimed = false
            }
        }

        frameHitField.valueFactory = LongValueFactory(0)
        frameHitField.valueProperty!!.bindBidirectional(model.frameHitProperty)
        frameHitField.parent.disableProperty().bind(timerState.runningProperty)
        frameHitField.setOnFocusLost(frameHitField::commitValue)
        frameHitField.text = ""

        timerState.runningProperty.asFlux()
            .subscribe { isPrimed = it }
    }
}