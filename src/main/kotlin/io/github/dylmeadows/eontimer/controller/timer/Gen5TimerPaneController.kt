package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
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
class Gen5TimerPaneController @Autowired constructor(
    private val model: Gen5TimerModel,
    private val timerService: TimerService) : TimerPaneController {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen5TimerMode>
    @FXML
    private lateinit var calibrationField: TextField
    @FXML
    private lateinit var targetDelayField: TextField
    @FXML
    private lateinit var targetSecondField: TextField
    @FXML
    private lateinit var entralinkCalibrationField: TextField
    @FXML
    private lateinit var frameCalibrationField: TextField
    @FXML
    private lateinit var targetAdvancesField: TextField
    @FXML
    private lateinit var secondHitField: TextField
    @FXML
    private lateinit var delayHitField: TextField
    @FXML
    private lateinit var actualAdvancesField: TextField

    override val canUpdate: BooleanBinding
        get() = timerService.runningProperty.not()

    fun initialize() {
        modeField.asChoiceField().valueProperty()
            .bindBidirectional(model.modeProperty)
        calibrationField.asIntField().valueProperty
            .bindBidirectional(model.calibrationProperty)
        targetDelayField.asIntField().valueProperty
            .bindBidirectional(model.targetDelayProperty)
        targetSecondField.asIntField().valueProperty
            .bindBidirectional(model.targetSecondProperty)
        entralinkCalibrationField.asIntField().valueProperty
            .bindBidirectional(model.entralinkCalibrationProperty)
        frameCalibrationField.asIntField().valueProperty
            .bindBidirectional(model.frameCalibrationProperty)
        targetAdvancesField.asIntField().valueProperty
            .bindBidirectional(model.targetAdvancesProperty)
        secondHitField.asIntField().valueProperty
            .bindBidirectional(model.secondHitProperty)
        delayHitField.asIntField().valueProperty
            .bindBidirectional(model.delayHitProperty)
        actualAdvancesField.asIntField().valueProperty
            .bindBidirectional(model.actualAdvancesProperty)

        secondHitField.text = ""
        delayHitField.text = ""
        actualAdvancesField.text = ""
    }
}