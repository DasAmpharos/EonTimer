package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.common.javafx.util.Choice
import io.github.dylmeadows.common.javafx.util.ChoiceConverter
import javafx.collections.FXCollections
import javafx.scene.control.ChoiceBox

inline fun <reified T> ChoiceBox<T>.asChoiceField(): ChoiceBox<T>
    where T : Enum<T>,
          T : Choice {
    val choices = T::class.java.enumConstants
    items = FXCollections.observableArrayList(*choices)
    converter = ChoiceConverter.forChoice(T::class.java)
    return this
}
