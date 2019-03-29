package io.github.dylmeadows.eontimer.util

import io.github.dylmeadows.common.javafx.util.Nodes
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.*
import javafx.beans.value.*
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.stage.Stage
import reactor.core.publisher.Flux
import reactor.util.function.Tuple2
import kotlin.reflect.KProperty

operator fun <T> ObservableValue<T>.getValue(thisRef: Any, property: KProperty<*>) = value
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

fun Spinner<Int>.createValueFactory(min: Int, max: Int): SpinnerValueFactory<Int> {
    valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(min, max)
    return valueFactory
}

fun Spinner<Int>.createValueFactory(min: Int, max: Int, initialValue: Int): SpinnerValueFactory<Int> {
    valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue)
    return valueFactory
}

fun ObjectProperty<Int>.bindBidirectional(property: IntegerProperty) {
    bindBidirectional(property.asObject())
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

fun Node.hideWhen(condition: BooleanBinding) {
    Nodes.hideAndResizeParentIf(this, condition)
}

var Label.isActive: Boolean
    get() = this.styleClass.contains("active")
    set(value) {
        when (value) {
            true -> styleClass.add("active")
            false -> styleClass.remove("active")
        }
    }
