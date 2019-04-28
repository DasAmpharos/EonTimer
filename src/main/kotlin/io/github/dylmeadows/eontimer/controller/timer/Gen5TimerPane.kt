package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.service.factory.Gen5TimerFactory
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.spinner.LongValueFactory
import io.github.dylmeadows.eontimer.util.javafx.spinner.commitValue
import io.github.dylmeadows.eontimer.util.javafx.spinner.setOnFocusLost
import io.github.dylmeadows.eontimer.util.javafx.spinner.text
import io.github.dylmeadows.eontimer.util.javafx.spinner.valueProperty
import io.github.dylmeadows.eontimer.util.showWhen
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen5TimerPane @Autowired constructor(
    private val model: Gen5TimerModel,
    private val timerState: TimerState,
    private val timerFactory: Gen5TimerFactory) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen5TimerMode>
    @FXML
    private lateinit var calibrationField: Spinner<Long>
    @FXML
    private lateinit var targetDelayField: Spinner<Long>
    @FXML
    private lateinit var targetSecondField: Spinner<Long>
    @FXML
    private lateinit var entralinkCalibrationField: Spinner<Long>
    @FXML
    private lateinit var frameCalibrationField: Spinner<Long>
    @FXML
    private lateinit var targetAdvancesField: Spinner<Long>
    @FXML
    private lateinit var secondHitField: Spinner<Long>
    @FXML
    private lateinit var delayHitField: Spinner<Long>
    @FXML
    private lateinit var actualAdvancesField: Spinner<Long>

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibrationField.valueFactory = LongValueFactory()
        calibrationField.valueProperty!!.bindBidirectional(model.calibrationProperty)
        calibrationField.parent.disableProperty().bind(timerState.runningProperty)
        calibrationField.setOnFocusLost(calibrationField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(0)
        targetDelayField.valueProperty!!.bindBidirectional(model.targetDelayProperty)
        targetDelayField.parent.disableProperty().bind(timerState.runningProperty)
        targetDelayField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.C_GEAR)
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENTRALINK))
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK)))
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(0)
        targetSecondField.valueProperty!!.bindBidirectional(model.targetSecondProperty)
        targetSecondField.parent.disableProperty().bind(timerState.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        entralinkCalibrationField.valueFactory = LongValueFactory()
        entralinkCalibrationField.valueProperty!!.bindBidirectional(model.entralinkCalibrationProperty)
        entralinkCalibrationField.parent.disableProperty().bind(timerState.runningProperty)
        entralinkCalibrationField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.ENTRALINK)
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK)))
        entralinkCalibrationField.setOnFocusLost(entralinkCalibrationField::commitValue)

        frameCalibrationField.valueFactory = LongValueFactory()
        frameCalibrationField.valueProperty!!.bindBidirectional(model.frameCalibrationProperty)
        frameCalibrationField.parent.disableProperty().bind(timerState.runningProperty)
        frameCalibrationField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))
        frameCalibrationField.setOnFocusLost(frameCalibrationField::commitValue)

        targetAdvancesField.valueFactory = LongValueFactory(0)
        targetAdvancesField.valueProperty!!.bindBidirectional(model.targetAdvancesProperty)
        targetAdvancesField.parent.disableProperty().bind(timerState.runningProperty)
        targetAdvancesField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))
        targetAdvancesField.setOnFocusLost(targetAdvancesField::commitValue)

        secondHitField.valueFactory = LongValueFactory(0)
        secondHitField.valueProperty!!.bindBidirectional(model.secondHitProperty)
        secondHitField.parent.disableProperty().bind(timerState.runningProperty)
        secondHitField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.STANDARD)
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENTRALINK))
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK)))
        secondHitField.setOnFocusLost(secondHitField::commitValue)
        secondHitField.text = ""

        delayHitField.valueFactory = LongValueFactory(0)
        delayHitField.valueProperty!!.bindBidirectional(model.delayHitProperty)
        delayHitField.parent.disableProperty().bind(timerState.runningProperty)
        delayHitField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.C_GEAR)
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENTRALINK))
                .or(model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK)))
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""

        actualAdvancesField.valueFactory = LongValueFactory(0)
        actualAdvancesField.valueProperty!!.bindBidirectional(model.actualAdvancesProperty)
        actualAdvancesField.parent.disableProperty().bind(timerState.runningProperty)
        actualAdvancesField.parent.showWhen(
            model.modeProperty.isEqualTo(Gen5TimerMode.ENHANCED_ENTRALINK))
        actualAdvancesField.setOnFocusLost(actualAdvancesField::commitValue)
        actualAdvancesField.text = ""
    }

    fun calibrate() {
        timerFactory.calibrate()
        secondHitField.text = ""
        delayHitField.text = ""
        actualAdvancesField.text = ""
    }
}