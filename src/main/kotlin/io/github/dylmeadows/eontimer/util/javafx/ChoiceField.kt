package io.github.dylmeadows.eontimer.util.javafx

import io.github.dylmeadows.common.javafx.util.Choice
import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.ObjectProperty
import javafx.collections.FXCollections
import javafx.scene.control.ChoiceBox

class ChoiceField<T>(choiceBox: ChoiceBox<T>)
    where T : Enum<T>,
          T : Choice {

    val valueProperty: ObjectProperty<T> = choiceBox.valueProperty()
    var value: T by valueProperty
}

inline fun <reified T> ChoiceBox<T>.asChoiceField(): ChoiceField<T>
    where T : Enum<T>,
          T : Choice {
    val choices = T::class.java.enumConstants
    items = FXCollections.observableArrayList(*choices)
    converter = ChoiceConverter.forChoice(T::class.java)
    return ChoiceField(this)
}
