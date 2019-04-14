package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.asLongField
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen4TimerPaneController @Autowired constructor(
    private val model: Gen4TimerModel) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen4TimerMode>
    @FXML
    private lateinit var calibratedDelayField: TextField
    @FXML
    private lateinit var calibratedSecondField: TextField
    @FXML
    private lateinit var targetDelayField: TextField
    @FXML
    private lateinit var targetSecondField: TextField
    @FXML
    private lateinit var delayHitField: TextField

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        calibratedDelayField.asLongField().valueProperty
            .bindBidirectional(model.calibratedDelayProperty)
        calibratedSecondField.asLongField().valueProperty
            .bindBidirectional(model.calibratedSecondProperty)
        targetDelayField.asLongField().valueProperty
            .bindBidirectional(model.targetDelayProperty)
        targetSecondField.asLongField().valueProperty
            .bindBidirectional(model.targetSecondProperty)
        delayHitField.asLongField().valueProperty
            .bindBidirectional(model.delayHitProperty)
        delayHitField.text = ""
    }
}