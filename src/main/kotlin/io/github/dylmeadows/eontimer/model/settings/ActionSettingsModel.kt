package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty

class ActionSettingsModel {
    @Transient
    val modeProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_MODE)
    var mode by modeProperty
    @Transient
    val colorProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_COLOR)
    var color by colorProperty
    @Transient
    val soundProperty = SimpleObjectProperty(ActionSettingsConstants.DEFAULT_SOUND)
    var sound by soundProperty
    @Transient
    val intervalProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_INTERVAL)
    var interval by intervalProperty
    @Transient
    val countProperty = SimpleIntegerProperty(ActionSettingsConstants.DEFAULT_COUNT)
    var count by countProperty
}