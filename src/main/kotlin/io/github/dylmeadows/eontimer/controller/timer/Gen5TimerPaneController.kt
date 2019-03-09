package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.createValueFactory
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen5TimerPaneController @Autowired constructor(
    private val model: Gen5TimerModel) : TimerPaneController {

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
        modeField.items = FXCollections.observableArrayList(*Gen5TimerMode.values())
        modeField.converter = ChoiceConverter.forChoice(Gen5TimerMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty)

        calibrationField.createValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.calibrationProperty)
        targetDelayField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.targetDelayProperty)
        targetSecondField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.targetSecondProperty)
        entralinkCalibrationField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.entralinkCalibrationProperty)
        frameCalibrationField.createValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.frameCalibrationProperty)
        targetAdvancesField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.targetAdvancesProperty)

        secondHitField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.secondHitProperty)
        delayHitField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.delayHitProperty)
        actualAdvancesField.createValueFactory(0, Integer.MAX_VALUE)
            .valueProperty().bindBidirectional(model.actualAdvancesProperty)
    }
}