package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.util.Spinners
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen5TimerController @Autowired constructor(
    private val model: Gen5TimerModel) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen5TimerMode>
    @FXML
    private lateinit var calibrationField: Spinner<Int>
    @FXML
    private lateinit var targetDelayField: Spinner<Int>
    @FXML
    private lateinit var targetSecondField: Spinner<Int>
    @FXML
    private lateinit var entralinkCalibrationField: Spinner<Int>
    @FXML
    private lateinit var frameCalibrationField: Spinner<Int>
    @FXML
    private lateinit var targetAdvancesField: Spinner<Int>
    @FXML
    private lateinit var secondHitField: Spinner<Int>
    @FXML
    private lateinit var delayHitField: Spinner<Int>
    @FXML
    private lateinit var actualAdvancesField: Spinner<Int>

    @FXML
    private lateinit var modeFieldSet: VBox
    @FXML
    private lateinit var calibrationFieldSet: VBox
    @FXML
    private lateinit var targetDelayFieldSet: VBox
    @FXML
    private lateinit var targetSecondFieldSet: VBox
    @FXML
    private lateinit var entralinkCalibrationFieldSet: VBox
    @FXML
    private lateinit var frameCalibrationFieldSet: VBox
    @FXML
    private lateinit var targetAdvancesFieldSet: VBox
    @FXML
    private lateinit var secondHitFieldSet: VBox
    @FXML
    private lateinit var delayHitFieldSet: VBox
    @FXML
    private lateinit var actualAdvancesFieldSet: VBox

    fun initialize() {
        // Mode
        modeField.items = FXCollections.observableArrayList(*Gen5TimerMode.values())
        modeField.converter = ChoiceConverter.forChoice(Gen5TimerMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty())
        // Calibration
        val calibrationValueFactory = Spinners.createValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_CALIBRATION)
        calibrationValueFactory.valueProperty().bindBidirectional(model.calibrationProperty().asObject())
        calibrationField.valueFactory = calibrationValueFactory
        // Target Delay
        val targetDelayValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_DELAY)
        targetDelayValueFactory.valueProperty().bindBidirectional(model.targetDelayProperty().asObject())
        targetDelayField.valueFactory = targetDelayValueFactory
        // Target Second
        val targetSecondValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_SECOND)
        targetSecondValueFactory.valueProperty().bindBidirectional(model.targetSecondProperty().asObject())
        targetSecondField.valueFactory = targetSecondValueFactory
        // Entralink Calibration
        val entralinkCalibrationValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_ENTRALINK_CALIBRATION)
        entralinkCalibrationValueFactory.valueProperty().bindBidirectional(model.entralinkCalibrationProperty().asObject())
        entralinkCalibrationField.valueFactory = entralinkCalibrationValueFactory
        // Frame Calibration
        val frameCalibrationValueFactory = Spinners.createValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_FRAME_CALIBRATION)
        frameCalibrationValueFactory.valueProperty().bindBidirectional(model.frameCalibrationProperty().asObject())
        frameCalibrationField.valueFactory = frameCalibrationValueFactory
        // Target Advances
        val targetAdvancesValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen5TimerConstants.DEFAULT_TARGET_ADVANCES)
        targetAdvancesValueFactory.valueProperty().bindBidirectional(model.targetAdvancesProperty().asObject())
        targetAdvancesField.valueFactory = targetAdvancesValueFactory
        // Second Hit
        val secondHitValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE)
        secondHitValueFactory.valueProperty().bindBidirectional(model.secondHitProperty().asObject())
        secondHitField.valueFactory = secondHitValueFactory
        // Delay Hit
        val delayHitValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE)
        delayHitValueFactory.valueProperty().bindBidirectional(model.delayHitProperty().asObject())
        delayHitField.valueFactory = delayHitValueFactory
        // Actual Advances
        val actualAdvancesValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE)
        actualAdvancesValueFactory.valueProperty().bindBidirectional(model.actualAdvancesProperty().asObject())
        actualAdvancesField.valueFactory = actualAdvancesValueFactory
    }
}