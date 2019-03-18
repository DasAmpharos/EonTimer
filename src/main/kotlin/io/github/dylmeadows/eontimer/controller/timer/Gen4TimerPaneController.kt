package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
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
class Gen4TimerPaneController @Autowired constructor(
    private val model: Gen4TimerModel,
    private val timerService: TimerService) : TimerPaneController {

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

    override val canUpdate: BooleanBinding
        get() = timerService.runningProperty.not()

    fun initialize() {
        modeField.asChoiceField().valueProperty()
            .bindBidirectional(model.modeProperty)
        calibratedDelayField.asIntField().valueProperty
            .bindBidirectional(model.calibratedDelayProperty)
        calibratedSecondField.asIntField().valueProperty
            .bindBidirectional(model.calibratedSecondProperty)
        targetDelayField.asIntField().valueProperty
            .bindBidirectional(model.targetDelayProperty)
        targetSecondField.asIntField().valueProperty
            .bindBidirectional(model.targetSecondProperty)
        delayHitField.asIntField().valueProperty
            .bindBidirectional(model.delayHitProperty)

        delayHitField.text = ""
    }
}