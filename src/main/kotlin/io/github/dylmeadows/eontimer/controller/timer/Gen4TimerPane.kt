package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.spinner.LongValueFactory
import io.github.dylmeadows.eontimer.util.javafx.spinner.commitValue
import io.github.dylmeadows.eontimer.util.javafx.spinner.setOnFocusLost
import io.github.dylmeadows.eontimer.util.javafx.spinner.text
import io.github.dylmeadows.eontimer.util.javafx.spinner.valueProperty
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen4TimerPane @Autowired constructor(
    private val model: Gen4TimerModel,
    private val timerState: TimerState) {

    @FXML
    private lateinit var modeField: ChoiceBox<Gen4TimerMode>
    @FXML
    private lateinit var calibratedDelayField: Spinner<Long>
    @FXML
    private lateinit var calibratedSecondField: Spinner<Long>
    @FXML
    private lateinit var targetDelayField: Spinner<Long>
    @FXML
    private lateinit var targetSecondField: Spinner<Long>
    @FXML
    private lateinit var delayHitField: Spinner<Long>

    fun initialize() {
        modeField.asChoiceField().valueProperty
            .bindBidirectional(model.modeProperty)
        modeField.parent.disableProperty().bind(timerState.runningProperty)

        calibratedDelayField.valueFactory = LongValueFactory()
        calibratedDelayField.valueProperty!!.bindBidirectional(model.calibratedDelayProperty)
        calibratedDelayField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedDelayField.setOnFocusLost(calibratedDelayField::commitValue)

        calibratedSecondField.valueFactory = LongValueFactory()
        calibratedSecondField.valueProperty!!.bindBidirectional(model.calibratedSecondProperty)
        calibratedSecondField.parent.disableProperty().bind(timerState.runningProperty)
        calibratedSecondField.setOnFocusLost(calibratedSecondField::commitValue)

        targetDelayField.valueFactory = LongValueFactory(0)
        targetDelayField.valueProperty!!.bindBidirectional(model.targetDelayProperty)
        targetDelayField.parent.disableProperty().bind(timerState.runningProperty)
        targetDelayField.setOnFocusLost(targetDelayField::commitValue)

        targetSecondField.valueFactory = LongValueFactory(0)
        targetSecondField.valueProperty!!.bindBidirectional(model.targetSecondProperty)
        targetSecondField.parent.disableProperty().bind(timerState.runningProperty)
        targetSecondField.setOnFocusLost(targetSecondField::commitValue)

        delayHitField.valueFactory = LongValueFactory(0)
        delayHitField.valueProperty!!.bindBidirectional(model.delayHitProperty)
        delayHitField.parent.disableProperty().bind(timerState.runningProperty)
        delayHitField.setOnFocusLost(delayHitField::commitValue)
        delayHitField.text = ""
    }
}