package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.common.javafx.util.Choice
import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import javafx.beans.property.ObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.ChoiceBox
import kotlin.reflect.KClass

class ChoiceField<T>
constructor(val choiceBox: ChoiceBox<T>,
            val choiceType: KClass<T>)
    where T : Enum<T>,
          T : Choice {

    val choices: ObservableList<T> = choiceBox.items
    val converter: ChoiceConverter<T> = choiceBox.converter as ChoiceConverter<T>

    val valueProperty: ObjectProperty<T> = choiceBox.valueProperty()
    var value: T by valueProperty
}

inline fun <reified T> ChoiceBox<T>.asChoiceField(): ChoiceField<T>
    where T : Enum<T>,
          T : Choice {
    val choices = T::class.java.enumConstants
    items = FXCollections.observableArrayList(*choices)
    converter = ChoiceConverter.forChoice(T::class.java)
    return ChoiceField(this, T::class)
}
