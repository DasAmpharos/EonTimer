package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.IntValueFactory
import io.github.dylmeadows.eontimer.util.LongValueFactory
import io.github.dylmeadows.eontimer.util.bindBidirectional
import io.github.dylmeadows.eontimer.util.javafx.asChoiceField
import io.github.dylmeadows.eontimer.util.javafx.asIntField
import io.github.dylmeadows.eontimer.util.valueProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ActionSettingsPane @Autowired constructor(
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
        modeField.asChoiceField().valueProperty.bindBidirectional(model.modeProperty)

        soundField.asChoiceField().valueProperty.bindBidirectional(model.soundProperty)

        colorField.valueProperty().bindBidirectional(model.colorProperty)

        intervalField.valueFactory = IntValueFactory(0, 1000)
        intervalField.valueProperty!!.bindBidirectional(model.intervalProperty)

        countField.valueFactory = IntValueFactory(0, 50)
        countField.valueProperty!!.bindBidirectional(model.countProperty)
    }
}