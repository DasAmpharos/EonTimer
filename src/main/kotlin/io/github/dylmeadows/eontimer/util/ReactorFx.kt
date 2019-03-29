package io.github.dylmeadows.eontimer.util

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import reactor.util.function.Tuple3
import reactor.util.function.Tuples

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

fun <T1, T2> anyChangesOf(property1: ObservableValue<T1>,
                          property2: ObservableValue<T2>): Flux<Tuple2<T1, T2>> {
    return anyChangesOf(property1, property2, Tuples::of)
}

fun <T1, T2, R> anyChangesOf(property1: ObservableValue<T1>,
                             property2: ObservableValue<T2>,
                             mapper: (T1, T2) -> R): Flux<R> {
    return Flux.create { emitter ->
        val d1 = property1.asFlux()
            .map { mapper(property1.value, property2.value) }
            .subscribe { emitter.next(it) }
        val d2 = property2.asFlux()
            .map { mapper(property1.value, property2.value) }
            .subscribe { emitter.next(it) }
        emitter.onDispose {
            d1.dispose()
            d2.dispose()
        }
    }
}

fun <T1, T2, T3> anyChangesOf(property1: ObservableValue<T1>,
                              property2: ObservableValue<T2>,
                              property3: ObservableValue<T3>): Flux<Tuple3<T1, T2, T3>> {
    return Flux.create { emitter ->
        val d1 = property1.asFlux()
            .map { combine(property1, property2, property3) }
            .subscribe { emitter.next(it) }
        val d2 = property2.asFlux()
            .map { combine(property1, property2, property3) }
            .subscribe { emitter.next(it) }
        val d3 = property3.asFlux()
            .map { combine(property1, property2, property3) }
            .subscribe { emitter.next(it) }
        emitter.onDispose {
            d1.dispose()
            d2.dispose()
            d3.dispose()
        }
    }
}

private fun <T1, T2, T3> combine(property1: ObservableValue<T1>,
                                 property2: ObservableValue<T2>,
                                 property3: ObservableValue<T3>): Tuple3<T1, T2, T3> {
    return Tuples.of(property1.value, property2.value, property3.value)
}

object JavaFxScheduler {

    private val platform = Schedulers.fromExecutor(Platform::runLater)

    fun platform(): Scheduler {
        return this.platform
    }
}
