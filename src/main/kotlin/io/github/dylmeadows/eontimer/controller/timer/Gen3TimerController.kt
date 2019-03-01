package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.common.javafx.util.Nodes
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerConstants
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.util.Spinners
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerController @Autowired constructor(
    private val model: Gen3TimerModel) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen3TimerMode>
    @FXML
    private lateinit var calibrationField: Spinner<Int>
    @FXML
    private lateinit var preTimerField: Spinner<Int>
    @FXML
    private lateinit var targetFrameField: Spinner<Int>
    @FXML
    private lateinit var frameHitField: Spinner<Int>

    @FXML
    private lateinit var calibrationFieldSet: VBox
    @FXML
    private lateinit var frameHitFieldSet: VBox

    fun initialize() {
        // Mode
        modeField.items = FXCollections.observableArrayList(*Gen3TimerMode.values())
        modeField.converter = ChoiceConverter.forChoice(Gen3TimerMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty())
        // Calibration
        val calibrationValueFactory = Spinners.createValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_CALIBRATION)
        calibrationValueFactory.valueProperty().bindBidirectional(model.calibrationProperty().asObject())
        calibrationField.valueFactory = calibrationValueFactory
        // Pre-Timer
        val preTimerValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_PRE_TIMER)
        preTimerValueFactory.valueProperty().bindBidirectional(model.preTimerProperty().asObject())
        preTimerField.valueFactory = preTimerValueFactory
        // Target Frame
        val targetFrameValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE, Gen3TimerConstants.DEFAULT_TARGET_FRAME)
        targetFrameValueFactory.valueProperty().bindBidirectional(model.targetFrameProperty().asObject())
        targetFrameField.valueFactory = targetFrameValueFactory
        // Frame Hit
        val frameHitValueFactory = Spinners.createValueFactory(0, Integer.MAX_VALUE)
        frameHitValueFactory.valueProperty().bindBidirectional(model.frameHitProperty().asObject())
        frameHitField.valueFactory = frameHitValueFactory

        // set conditional field visibility
        val isStandardMode = modeField.valueProperty().isNotEqualTo(Gen3TimerMode.STANDARD)
        Nodes.hideAndResizeParentIf(calibrationFieldSet, isStandardMode)
        Nodes.hideAndResizeParentIf(frameHitFieldSet, isStandardMode)
    }
}