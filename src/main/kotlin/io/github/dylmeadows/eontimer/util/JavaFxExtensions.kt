package io.github.dylmeadows.eontimer.util

import com.sun.javafx.binding.BidirectionalBinding
import io.github.dylmeadows.common.javafx.util.Nodes
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.FloatProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableDoubleValue
import javafx.beans.value.ObservableFloatValue
import javafx.beans.value.ObservableIntegerValue
import javafx.beans.value.ObservableLongValue
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.stage.Stage
import javafx.util.converter.IntegerStringConverter
import javafx.util.converter.LongStringConverter
import java.util.*
import kotlin.reflect.KProperty

typealias IntProperty = IntegerProperty

operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>): T = this.value!!
operator fun <T> Property<T>.setValue(thisRef: Any, property: KProperty<*>, value: T?) = setValue(value)

operator fun ObservableDoubleValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun DoubleProperty.setValue(thisRef: Any, property: KProperty<*>, value: Double) = set(value)

operator fun ObservableFloatValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun FloatProperty.setValue(thisRef: Any, property: KProperty<*>, value: Float) = set(value)

operator fun ObservableLongValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun LongProperty.setValue(thisRef: Any, property: KProperty<*>, value: Long) = set(value)

operator fun ObservableIntegerValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun IntegerProperty.setValue(thisRef: Any, property: KProperty<*>, value: Int) = set(value)

operator fun ObservableBooleanValue.getValue(thisRef: Any, property: KProperty<*>) = get()
operator fun BooleanProperty.setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = set(value)

val <T> Spinner<T>.valueProperty: ObjectProperty<T>?
    get() = valueFactory?.valueProperty()

fun ObjectProperty<Int>.asIntProperty(): IntProperty {
    return IntegerProperty.integerProperty(this)
}

fun IntProperty.bindBidirectional(property: ObjectProperty<Int>) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Int>.bindBidirectional(property: IntProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.asLongProperty(): LongProperty {
    return LongProperty.longProperty(this)
}

fun LongProperty.bindBidirectional(property: ObjectProperty<Long>) {
    BidirectionalBinding.bindNumber(this, property)
}

fun ObjectProperty<Long>.bindBidirectional(property: LongProperty) {
    BidirectionalBinding.bindNumber(this, property)
}

class IntValueFactory(min: Int = Int.MIN_VALUE,
                      max: Int = Int.MAX_VALUE,
                      initialValue: Int = 0,
                      step: Int = 1) : SpinnerValueFactory<Int>() {

    val stepProperty: IntProperty = SimpleIntegerProperty(step)
    val minProperty: IntProperty = object : SimpleIntegerProperty(min) {
        override fun invalidated() {
            val newMin = get()
            if (newMin > this@IntValueFactory.max) {
                this@IntValueFactory.min = this@IntValueFactory.max
                return
            }
            if (this@IntValueFactory.value < newMin) {
                this@IntValueFactory.value = newMin
            }
        }
    }
    val maxProperty: IntProperty = object : SimpleIntegerProperty(max) {
        override fun invalidated() {
            val newMax = get()
            if (newMax < this@IntValueFactory.min) {
                this@IntValueFactory.max = this@IntValueFactory.min
                return
            }

            if (this@IntValueFactory.value > newMax) {
                this@IntValueFactory.value = newMax
            }
        }
    }

    var step by stepProperty
    var min by minProperty
    var max by maxProperty

    init {
        converter = IntegerStringConverter()
        valueProperty().addListener { _, _, newValue ->
            // when the value is set, we need to react to ensure it is a
            // valid value (and if not, blow up appropriately)
            Optional.ofNullable(newValue)
                .ifPresent {
                    this@IntValueFactory.value = when {
                        newValue < this@IntValueFactory.min -> this@IntValueFactory.min
                        newValue > this@IntValueFactory.max -> this@IntValueFactory.max
                        else -> newValue
                    }
                }
        }
        value = if (initialValue in min..max) initialValue else min
    }

    override fun increment(steps: Int) {
        Optional.ofNullable(value)
            .map { it + (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue <= max -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) - 1
                    else -> max
                }
            }
    }

    override fun decrement(steps: Int) {
        Optional.ofNullable(value)
            .map { it - (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue >= min -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) + 1
                    else -> min
                }
            }
    }

    private fun wrapValue(value: Int, min: Int, max: Int): Int {
        if (max == 0) {
            throw RuntimeException()
        }

        val r = value % max
        return when (min) {
            in (max + 1).until(r) -> {
                r + max - min
            }
            in (r + 1).until(max) -> {
                r + max - min
            }
            else -> r
        }
    }
}

class LongValueFactory(min: Long = Long.MIN_VALUE,
                       max: Long = Long.MAX_VALUE,
                       initialValue: Long = 0,
                       step: Int = 1) : SpinnerValueFactory<Long>() {

    val stepProperty: IntegerProperty = SimpleIntegerProperty(step)
    val minProperty: LongProperty = object : SimpleLongProperty(min) {
        override fun invalidated() {
            val newMin = get()
            if (newMin > this@LongValueFactory.max) {
                this@LongValueFactory.min = this@LongValueFactory.max
                return
            }
            if (this@LongValueFactory.value < newMin) {
                this@LongValueFactory.value = newMin
            }
        }
    }
    val maxProperty: LongProperty = object : SimpleLongProperty(max) {
        override fun invalidated() {
            val newMax = get()
            if (newMax < this@LongValueFactory.min) {
                this@LongValueFactory.max = this@LongValueFactory.min
                return
            }

            if (this@LongValueFactory.value > newMax) {
                this@LongValueFactory.value = newMax
            }
        }
    }

    var step by stepProperty
    var min by minProperty
    var max by maxProperty

    init {
        converter = LongStringConverter()
        valueProperty().addListener { _, _, newValue ->
            // when the value is set, we need to react to ensure it is a
            // valid value (and if not, blow up appropriately)
            Optional.ofNullable(newValue)
                .ifPresent {
                    this@LongValueFactory.value = when {
                        newValue < this@LongValueFactory.min -> this@LongValueFactory.min
                        newValue > this@LongValueFactory.max -> this@LongValueFactory.max
                        else -> newValue
                    }
                }
        }
        value = if (initialValue in min..max) initialValue else min
    }

    override fun increment(steps: Int) {
        Optional.ofNullable(value)
            .map { it + (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue <= max -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) - 1
                    else -> max
                }
            }
    }

    override fun decrement(steps: Int) {
        Optional.ofNullable(value)
            .map { it - (steps * step) }
            .ifPresent { newValue ->
                value = when {
                    newValue >= min -> newValue
                    isWrapAround -> wrapValue(newValue, min, max) + 1
                    else -> min
                }
            }
    }

    private fun wrapValue(value: Long, min: Long, max: Long): Long {
        if (max == 0L) {
            throw RuntimeException()
        }

        val r = value % max
        return when (min) {
            in (max + 1).until(r) -> {
                r + max - min
            }
            in (r + 1).until(max) -> {
                r + max - min
            }
            else -> r
        }
    }
}

val <T> Spinner<T>.textProperty: StringProperty
    get() = editor.textProperty()
var <T> Spinner<T>.text: String
    get() = editor.text
    set(value) {
        editor.text = value
    }

fun <T : Parent> SpringJavaFxApplication.load(resource: FxmlResource): T {
    return load(resource.get())
}

fun Scene.addCss(resource: CssResource) {
    stylesheets.add(resource.path)
}

fun Parent.asScene(): Scene {
    return Scene(this)
}

data class Dimension(val width: Double, val height: Double)

var Stage.size: Dimension
    get() = Dimension(width, height)
    set(value) {
        width = value.width
        height = value.height
    }

fun Node.showWhen(condition: BooleanBinding) {
    Nodes.hideAndResizeParentIf(this, condition.not())
}

var Label.isActive: Boolean
    get() = this.styleClass.contains("active")
    set(value) {
        when (value) {
            true -> styleClass.add("active")
            false -> styleClass.remove("active")
        }
    }
