package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.asLongField
import io.github.dylmeadows.eontimer.util.hideWhen
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen5TimerPaneController @Autowired constructor(
    private val model: Gen5TimerModel) {

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

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        calibrationField.asLongField().valueProperty
            .bindBidirectional(model.calibrationProperty)
        targetDelayField.asLongField().valueProperty
            .bindBidirectional(model.targetDelayProperty)
        targetSecondField.asLongField().valueProperty
            .bindBidirectional(model.targetSecondProperty)
        entralinkCalibrationField.asLongField().valueProperty
            .bindBidirectional(model.entralinkCalibrationProperty)
        frameCalibrationField.asLongField().valueProperty
            .bindBidirectional(model.frameCalibrationProperty)
        targetAdvancesField.asLongField().valueProperty
            .bindBidirectional(model.targetAdvancesProperty)
        secondHitField.asLongField().valueProperty
            .bindBidirectional(model.secondHitProperty)
        delayHitField.asLongField().valueProperty
            .bindBidirectional(model.delayHitProperty)
        actualAdvancesField.asLongField().valueProperty
            .bindBidirectional(model.actualAdvancesProperty)
        secondHitField.text = ""
        delayHitField.text = ""
        actualAdvancesField.text = ""

        targetDelayField.parent.hideWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.STANDARD))
        entralinkCalibrationField.parent.hideWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.STANDARD)
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.C_GEAR)))
        frameCalibrationField.parent.hideWhen(
            model.modeProperty.isNotEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))
        targetAdvancesField.parent.hideWhen(
            model.modeProperty.isNotEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))

        secondHitField.parent.hideWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.C_GEAR))
        delayHitField.parent.hideWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.STANDARD))
        actualAdvancesField.parent.hideWhen(
            model.modeProperty.isNotEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))
    }
}