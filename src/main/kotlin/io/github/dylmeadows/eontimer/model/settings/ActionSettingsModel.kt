package io.github.dylmeadows.eontimer.model.settings

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.getValue
import tornadofx.setValue

class ActionSettingsModel {
    @Transient
    val modeProperty = SimpleObjectProperty(DEFAULT_MODE)
    var mode by modeProperty
    @Transient
    val colorProperty = SimpleObjectProperty(DEFAULT_COLOR)
    var color by colorProperty
    @Transient
    val soundProperty = SimpleObjectProperty(DEFAULT_SOUND)
    var sound by soundProperty
    @Transient
    val intervalProperty = SimpleIntegerProperty(DEFAULT_INTERVAL)
    var interval by intervalProperty
    @Transient
    val countProperty = SimpleIntegerProperty(DEFAULT_COUNT)
    var count by countProperty
}