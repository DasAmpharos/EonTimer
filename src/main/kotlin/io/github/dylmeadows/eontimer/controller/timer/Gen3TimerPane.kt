package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.factory.Gen3TimerFactory
import io.github.dylmeadows.eontimer.service.factory.timer.VariableFrameTimer
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.asChoiceField
import io.github.dylmeadows.eontimer.util.asLongField
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerPane @Autowired constructor(
    private val state: TimerState,
    private val model: Gen3TimerModel,
    private val factory: Gen3TimerFactory,
    private val variableFrameTimer: VariableFrameTimer) : TimerController {

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
        /*calibrationField.asLongField().valueProperty
            .bindBidirectional(model.calibrationProperty)
        preTimerField.asLongField().valueProperty
            .bindBidirectional(model.preTimerProperty)
        targetFrameField.asLongField().valueProperty
            .bindBidirectional(model.targetFrameProperty)
        frameHitField.asLongField().valueProperty
            .bindBidirectional(model.frameHitProperty)
        frameHitField.text = ""

        var previousTargetFrame = model.targetFrame
        model.modeProperty.asFlux()
            .subscribe {
                if (it == Gen3TimerMode.VARIABLE_TARGET) {
                    previousTargetFrame = model.targetFrame
                    model.targetFrame = 0
                    targetFrameField.text = ""
                } else {
                    model.targetFrame = previousTargetFrame
                }
            }*/

        val targetFrameProperty = targetFrameField.asLongField().valueProperty
        setTargetFrameBtn.setOnAction {
            if (state.running && variableFrameTimer.targetFrame < 0) {
                variableFrameTimer.targetFrame = targetFrameProperty.value
            }
        }

        // set conditional field visibility
        /*setTargetFrameBtn.hideWhen(model.modeProperty.isNotEqualTo(Gen3TimerMode.VARIABLE_TARGET))
        setTargetFrameBtn.disableProperty().bind(targetFrameField.textProperty().isEmpty
            .or(model.targetFrameProperty.lessThanOrEqualTo(0L)))*/
    }

    override fun start() {
        factory.start()
            .subscribeOn(JavaFxScheduler.platform())
            .publishOn(JavaFxScheduler.platform())
            .subscribe {
                state.remaining = it.remaining.toMillis()
            }
    }

    override fun stop() {
        variableFrameTimer.stop()
    }

    override fun update() {

    }
}