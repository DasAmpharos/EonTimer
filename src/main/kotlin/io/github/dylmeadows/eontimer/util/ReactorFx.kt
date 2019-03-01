package io.github.dylmeadows.eontimer.util

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

fun <T> ObservableValue<T>.changesAsFlux(): Flux<Change<T>> {
    return Flux.create { emitter ->
        val listener: ChangeListener<T> = ChangeListener { _, oldValue, newValue ->
            emitter.next(Change(oldValue, newValue))
        }
        this.addListener(listener)

        emitter.onDispose {
            Platform.runLater {
                this.removeListener(listener)
            }
        }
    }
}

data class Change<T>(val oldValue: T, val newValue: T)

object JavaFxScheduler {
    private val platform = Schedulers.fromExecutor(Platform::runLater)

    fun platform(): Scheduler {
        return this.platform
    }
}
