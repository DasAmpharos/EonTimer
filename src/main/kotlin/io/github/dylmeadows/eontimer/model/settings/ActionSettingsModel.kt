package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

data class ActionSettingsModel(
    val modeProperty: ObjectProperty<ActionMode> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_MODE),
    val colorProperty: ObjectProperty<Color> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_COLOR),
    val soundProperty: ObjectProperty<SoundResource> = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_SOUND),
    val intervalProperty: IntegerProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_INTERVAL),
    val countProperty: IntegerProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_COUNT)) {

    var mode by modeProperty
    var color by colorProperty
    var sound by soundProperty
    var interval by intervalProperty
    var count by countProperty
}