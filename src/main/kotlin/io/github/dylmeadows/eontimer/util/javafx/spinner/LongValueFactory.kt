package io.github.dylmeadows.eontimer.util.javafx.spinner

import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.IntegerProperty
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleLongProperty
import javafx.scene.control.SpinnerValueFactory
import javafx.util.converter.LongStringConverter
import java.util.*

class LongValueFactory(
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    initialValue: Long = 0L,
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