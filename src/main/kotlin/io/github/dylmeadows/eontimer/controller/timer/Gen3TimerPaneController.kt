package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.util.asChoiceField
import io.github.dylmeadows.eontimer.util.asIntField
import io.github.dylmeadows.eontimer.util.hideWhen
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerPaneController @Autowired constructor(
    private val model: Gen3TimerModel) {

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

    fun initialize() {
        modeField.asChoiceField().valueProperty
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
        calibrationField.parent.hideWhen(model.modeProperty.isNotEqualTo(Gen3TimerMode.STANDARD))
        frameHitField.parent.hideWhen(model.modeProperty.isNotEqualTo(Gen3TimerMode.STANDARD))
    }
}