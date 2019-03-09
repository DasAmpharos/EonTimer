package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color

class ActionSettingsModel {
    val modeProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_MODE)
    val colorProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_COLOR)
    val soundProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_SOUND)
    val intervalProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_INTERVAL)
    val countProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_COUNT)

    var mode: ActionMode by modeProperty
    var color: Color by colorProperty
    var sound: SoundResource by soundProperty
    var interval by intervalProperty
    var count by countProperty
}