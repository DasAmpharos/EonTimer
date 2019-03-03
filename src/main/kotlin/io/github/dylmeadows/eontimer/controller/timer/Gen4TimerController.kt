package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen4TimerController @Autowired constructor(
    private val model: Gen4TimerModel) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen4TimerMode>
    @FXML
    private lateinit var calibratedDelayField: Spinner<Int>
    @FXML
    private lateinit var calibratedSecondField: Spinner<Int>
    @FXML
    private lateinit var targetDelayField: Spinner<Int>
    @FXML
    private lateinit var targetSecondField: Spinner<Int>
    @FXML
    private lateinit var delayHitField: Spinner<Int>

    fun initialize() {
        // Mode
        modeField.items = FXCollections.observableArrayList(*Gen4TimerMode.values())
        modeField.converter = ChoiceConverter.forChoice(Gen4TimerMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty)
    }
}