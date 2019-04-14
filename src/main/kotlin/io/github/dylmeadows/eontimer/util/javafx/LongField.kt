package io.github.dylmeadows.eontimer.util.javafx

import com.google.common.primitives.Longs
import com.sun.javafx.binding.BidirectionalBinding
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.StringProperty
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.util.StringConverter
import java.util.*

class LongField
constructor(val textField: TextField,
            val valueProperty: LongProperty) {

    val textProperty: StringProperty = textField.textProperty()
    var text: String by textProperty

    var value: Long by valueProperty
}

fun TextField.asLongField(): LongField {
    textFormatter = TextFormatter<Int>(::validate)
    val valueProperty = SimpleLongProperty()
    BidirectionalBinding.bind(textProperty(), valueProperty, LongFieldStringConverter(valueProperty))
    return LongField(this, valueProperty)
}

private class LongFieldStringConverter
constructor(private val valueProperty: LongProperty) : StringConverter<Number>() {

    override fun toString(number: Number): String {
        return number.toString()
    }

    override fun fromString(string: String): Long {
        return Optional.ofNullable(Longs.tryParse(string))
            .orElseGet(valueProperty::getValue)
    }
}

private val LONG_REGEX = Regex("^-?\\d*$")

private fun validate(change: TextFormatter.Change): TextFormatter.Change? {
    return if (change.controlNewText.matches(LONG_REGEX)) change else null
}