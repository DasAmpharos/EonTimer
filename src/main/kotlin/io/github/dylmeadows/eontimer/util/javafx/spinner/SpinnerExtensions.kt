package io.github.dylmeadows.eontimer.util.javafx.spinner

import io.github.dylmeadows.eontimer.util.asFlux
import javafx.beans.property.ObjectProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.Spinner
import java.util.*

val <T> Spinner<T>.valueProperty: ObjectProperty<T>?
    get() = valueFactory?.valueProperty()

val <T> Spinner<T>.textProperty: StringProperty
    get() = editor.textProperty()
var <T> Spinner<T>.text: String
    get() = editor.text
    set(value) {
        editor.text = value
    }

fun <T> Spinner<T>.setValue(value: T) {
    valueFactory?.value = value
}

fun <T> Spinner<T>.commitValue() {
    Optional.ofNullable(valueFactory)
        .map { it.converter }
        .ifPresent { converter ->
            setValue(converter.fromString(text))
        }
}

fun <T> Spinner<T>.setOnFocusLost(onFocusLost: () -> Unit) {
    focusedProperty().asFlux()
        .filter { isFocused -> !isFocused }
        .subscribe { onFocusLost() }
}