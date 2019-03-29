package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.asIntField
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextField
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
    private lateinit var intervalField: TextField
    @FXML
    private lateinit var countField: TextField

    fun initialize() {
        modeField.items = FXCollections.observableArrayList(*ActionMode.values())
        modeField.converter = ChoiceConverter.forChoice(ActionMode::class.java)
        modeField.valueProperty().bindBidirectional(model.modeProperty)

        soundField.items = FXCollections.observableArrayList(*SoundResource.values())
        soundField.converter = ChoiceConverter.forChoice(SoundResource::class.java)
        soundField.valueProperty().bindBidirectional(model.soundProperty)

        colorField.valueProperty().bindBidirectional(model.colorProperty)

        intervalField.asIntField().valueProperty
            .bindBidirectional(model.intervalProperty)
        countField.asIntField().valueProperty
            .bindBidirectional(model.countProperty)
    }
}