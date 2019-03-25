package io.github.dylmeadows.eontimer.util

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

fun <T> ObservableValue<T>.asFlux(): Flux<T> {
    return Flux.create { emitter ->
        emitter.next(value)
        val listener: ChangeListener<T> = ChangeListener { _, _, newValue ->
            emitter.next(newValue)
        }
        addListener(listener)

        emitter.onDispose {
            Platform.runLater {
                removeListener(listener)
            }
        }
    }
}

fun <T> ObservableList<T>.asFlux(): Flux<List<T>> {
    return Flux.create { emitter ->
        emitter.next(this)
        val listener: ListChangeListener<T> = ListChangeListener { change ->
            emitter.next(change.list)
        }
        addListener(listener)

        emitter.onDispose {
            Platform.runLater {
                removeListener(listener)
            }
        }
    }
}

object JavaFxScheduler {

    private val platform = Schedulers.fromExecutor(Platform::runLater)

    fun platform(): Scheduler {
        return this.platform
    }
}
