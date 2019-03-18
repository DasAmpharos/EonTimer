package io.github.dylmeadows.eontimer.util

import com.google.common.primitives.Ints
import com.sun.javafx.binding.BidirectionalBinding
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.*
import java.util.regex.Pattern

private const val INTEGER_REGEX = "^-?\\d*$"
private val INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX)

class IntField {
    val valueConverter: StringConverter<Number> = object : StringConverter<Number>() {

        override fun toString(number: Number): String {
            return number.toString()
        }

        override fun fromString(string: String): Int {
            return Optional.ofNullable(Ints.tryParse(string))
                .orElseGet(valueProperty::getValue)
        }
    }
    val valueProperty: IntegerProperty = SimpleIntegerProperty()
    var value: Int by valueProperty
}

fun TextField.asIntField(): IntField {
    val intField = IntField()
    textFormatter = TextFormatter<Int>(::validate)
    BidirectionalBinding.bind(textProperty(), intField.valueProperty, intField.valueConverter)
    return intField
}

private fun validate(change: TextFormatter.Change): TextFormatter.Change? {
    return if (INTEGER_PATTERN.matcher(change.controlNewText).matches()) change else null
}