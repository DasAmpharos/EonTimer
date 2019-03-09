package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.common.javafx.util.Nodes
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerConstants
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.createValueFactory
import javafx.beans.binding.BooleanBinding
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import javafx.scene.layout.VBox
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen3TimerPaneController @Autowired constructor(
    private val model: Gen3TimerModel,
    private val timerService: TimerService) : TimerPaneController {

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

    override val canUpdate: BooleanBinding
        get() = timerService.runningProperty.not()
            .or(model.modeProperty.isEqualTo(Gen3TimerMode.VARIABLE_TARGET))

    fun initialize() {
        modeField.items = FXCollections.observableArrayList(*Gen3TimerMode.values())
        modeField.converter = ChoiceConverter.forChoice(Gen3TimerMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty)

        calibrationField.createValueFactory(Int.MIN_VALUE, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.calibrationProperty)
        preTimerField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.preTimerProperty)
        targetFrameField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.targetFrameProperty)
        frameHitField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.frameHitProperty)

        // set conditional field visibility
        val isStandardMode = modeField.valueProperty().isNotEqualTo(Gen3TimerMode.STANDARD)
        Nodes.hideAndResizeParentIf(calibrationFieldSet, isStandardMode)
        Nodes.hideAndResizeParentIf(frameHitFieldSet, isStandardMode)
    }
}