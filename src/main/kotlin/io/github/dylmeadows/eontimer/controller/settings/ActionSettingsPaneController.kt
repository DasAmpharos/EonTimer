package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsConstants
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.createValueFactory
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ActionSettingsPaneController @Autowired constructor(
    private val model: ActionSettingsModel) {

    @FXML
    private lateinit var modeField: ChoiceBox<ActionMode>
    @FXML
    private lateinit var soundField: ChoiceBox<SoundResource>
    @FXML
    private lateinit var colorField: ColorPicker
    @FXML
    private lateinit var intervalField: Spinner<Int>
    @FXML
    private lateinit var countField: Spinner<Int>

    fun initialize() {
        modeField.items = FXCollections.observableArrayList(*ActionMode.values())
        modeField.converter = ChoiceConverter.forChoice(ActionMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty)

        soundField.items = FXCollections.observableArrayList(*SoundResource.values())
        soundField.converter = ChoiceConverter.forChoice(SoundResource::class.java)
        soundField.valueProperty().bindBidirectional(model.soundProperty)

        colorField.valueProperty().bindBidirectional(model.colorProperty)

        intervalField.createValueFactory(0, Integer.MAX_VALUE)
            .valueProperty().bindBidirectional(model.intervalProperty.asObject())
        countField.createValueFactory(0, Integer.MAX_VALUE)
            .valueProperty().bindBidirectional(model.countProperty.asObject())
    }

    fun resetDefaultValues() {
        modeField.value = ActionSettingsConstants.DEFAULT_MODE
        soundField.value = ActionSettingsConstants.DEFAULT_SOUND
        colorField.value = ActionSettingsConstants.DEFAULT_COLOR
        intervalField.valueFactory.value = ActionSettingsConstants.DEFAULT_INTERVAL
        countField.valueFactory.value = ActionSettingsConstants.DEFAULT_COUNT
    }
}