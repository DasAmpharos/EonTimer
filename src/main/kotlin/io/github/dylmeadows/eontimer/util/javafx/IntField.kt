package io.github.dylmeadows.eontimer.util.javafx

import com.google.common.primitives.Ints
import com.sun.javafx.binding.BidirectionalBinding
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.*
import java.util.regex.Pattern

class IntField
constructor(val textField: TextField,
            val valueProperty: IntegerProperty) {

    val textProperty: StringProperty = textField.textProperty()
    var text: String by textProperty

    var value: Int by valueProperty
}

fun TextField.asIntField(): IntField {
    textFormatter = TextFormatter<Int>(::validate)
    val valueProperty = SimpleIntegerProperty()
    BidirectionalBinding.bind(textProperty(), valueProperty, IntFieldStringConverter(valueProperty))
    return IntField(this, valueProperty)
}

private class IntFieldStringConverter
constructor(private val valueProperty: IntegerProperty) : StringConverter<Number>() {

    override fun toString(number: Number): String {
        return number.toString()
    }

    override fun fromString(string: String): Int {
        return Optional.ofNullable(Ints.tryParse(string))
            .orElseGet(valueProperty::getValue)
    }
}

private const val INTEGER_REGEX = "^-?\\d*$"
private val INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX)

private fun validate(change: TextFormatter.Change): TextFormatter.Change? {
    return if (INTEGER_PATTERN.matcher(change.controlNewText).matches()) change else null
}