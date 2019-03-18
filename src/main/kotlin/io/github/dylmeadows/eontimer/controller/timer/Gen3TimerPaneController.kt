package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.Nodes
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.asChoiceField
import io.github.dylmeadows.eontimer.util.asIntField
import javafx.beans.binding.BooleanBinding
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerPaneController @Autowired constructor(
    private val model: Gen3TimerModel,
    private val timerService: TimerService) : TimerPaneController {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen3TimerMode>
    @FXML
    private lateinit var calibrationField: TextField
    @FXML
    private lateinit var preTimerField: TextField
    @FXML
    private lateinit var targetFrameField: TextField
    @FXML
    private lateinit var frameHitField: TextField

    override val canUpdate: BooleanBinding
        get() = timerService.runningProperty.not()
            .or(model.modeProperty.isEqualTo(Gen3TimerMode.VARIABLE_TARGET))

    fun initialize() {
        modeField.asChoiceField().valueProperty()
            .bindBidirectional(model.modeProperty)
        calibrationField.asIntField().valueProperty
            .bindBidirectional(model.calibrationProperty)
        preTimerField.asIntField().valueProperty
            .bindBidirectional(model.preTimerProperty)
        targetFrameField.asIntField().valueProperty
            .bindBidirectional(model.targetFrameProperty)
        frameHitField.asIntField().valueProperty
            .bindBidirectional(model.frameHitProperty)
        frameHitField.text = ""

        // set conditional field visibility
        val isStandardMode = modeField.valueProperty().isEqualTo(Gen3TimerMode.STANDARD)
        Nodes.hideAndResizeParentIf(calibrationField.parent, isStandardMode.not())
        Nodes.hideAndResizeParentIf(frameHitField.parent, isStandardMode.not())
    }
}