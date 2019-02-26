package io.github.dylmeadows.eontimer.model

import javafx.beans.property.SimpleObjectProperty
import org.springframework.stereotype.Component
import tornadofx.getValue
import tornadofx.setValue


@Component
class TimerModel {
    val stagesProperty: SimpleObjectProperty<List<Long>> = SimpleObjectProperty(ArrayList())
    var stages by stagesProperty
}