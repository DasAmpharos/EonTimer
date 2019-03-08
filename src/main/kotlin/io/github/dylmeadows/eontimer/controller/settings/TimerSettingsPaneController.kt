package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.settings.Console
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsConstants
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.util.createValueFactory
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerSettingsPaneController @Autowired constructor(
    private val model: TimerSettingsModel) {

    @FXML
    private lateinit var consoleField: ChoiceBox<Console>
    @FXML
    private lateinit var refreshIntervalField: Spinner<Int>
    @FXML
    private lateinit var precisionCalibrationField: CheckBox

    fun initialize() {
        consoleField.items = FXCollections.observableArrayList(*Console.values())
        consoleField.converter = ChoiceConverter.forChoice(Console::class.java)
        consoleField.valueProperty().bindBidirectional(model.consoleProperty)

        refreshIntervalField.createValueFactory(0, Int.MAX_VALUE)
            .valueProperty().bindBidirectional(model.refreshIntervalProperty.asObject())

        precisionCalibrationField.selectedProperty()
            .bindBidirectional(model.precisionCalibrationModeProperty)
    }
}