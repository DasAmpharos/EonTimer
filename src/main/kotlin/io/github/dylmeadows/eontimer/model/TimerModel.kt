package io.github.dylmeadows.eontimer.model

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component

@Component
class TimerModel {
    val stagesProperty: SimpleObjectProperty<List<Long>> = SimpleObjectProperty(ArrayList())
    var stages by stagesProperty
}