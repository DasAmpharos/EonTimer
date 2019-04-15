package io.github.dylmeadows.eontimer.util

import javafx.application.Platform
import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import reactor.util.function.Tuple3
import reactor.util.function.Tuple4
import reactor.util.function.Tuples
import java.util.stream.Stream
import kotlin.streams.toList

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
        val emit: (R) -> Unit = { emitter.next(it) }
        val mapToValue = { mapper(property1.value, property2.value) }
        val disposables = Stream.of(property1, property2)
            .map { it.asFlux() }
            .map { it.map { mapToValue() } }
            .map { it.subscribe(emit) }
            .toList()
        emitter.onDispose { disposables.forEach(Disposable::dispose) }
    }
}

fun <T1, T2, T3> anyChangesOf(property1: ObservableValue<T1>,
                              property2: ObservableValue<T2>,
                              property3: ObservableValue<T3>): Flux<Tuple3<T1, T2, T3>> {
    return anyChangesOf(property1, property2, property3, Tuples::of)
}

fun <T1, T2, T3, R> anyChangesOf(property1: ObservableValue<T1>,
                                 property2: ObservableValue<T2>,
                                 property3: ObservableValue<T3>,
                                 mapper: (T1, T2, T3) -> R): Flux<R> {
    return Flux.create { emitter ->
        val emit: (R) -> Unit = { emitter.next(it) }
        val mapToValue = { mapper(property1.value, property2.value, property3.value) }
        val disposables = Stream.of(property1, property2, property3)
            .map { it.asFlux() }
            .map { it.map { mapToValue() } }
            .map { it.subscribe(emit) }
            .toList()
        emitter.onDispose { disposables.forEach(Disposable::dispose) }
    }
}

fun <T1, T2, T3, T4> anyChangesOf(property1: ObservableValue<T1>,
                                  property2: ObservableValue<T2>,
                                  property3: ObservableValue<T3>,
                                  property4: ObservableValue<T4>): Flux<Tuple4<T1, T2, T3, T4>> {
    return anyChangesOf(property1, property2, property3, property4, Tuples::of)
}

fun <T1, T2, T3, T4, R> anyChangesOf(property1: ObservableValue<T1>,
                                     property2: ObservableValue<T2>,
                                     property3: ObservableValue<T3>,
                                     property4: ObservableValue<T4>,
                                     mapper: (T1, T2, T3, T4) -> R): Flux<R> {
    return Flux.create { emitter ->
        val emit: (R) -> Unit = { emitter.next(it) }
        val mapToValue = { mapper(property1.value, property2.value, property3.value, property4.value) }
        val disposables = Stream.of(property1, property2, property3, property4)
            .map { it.asFlux() }
            .map { it.map { mapToValue() } }
            .map { it.subscribe(emit) }
            .toList()
        emitter.onDispose { disposables.forEach(Disposable::dispose) }
    }
}

fun <T> Flux<T>.asObservableValue(initialValue: T): ObservableFluxValue<T> {
    return ObservableFluxValue(this, initialValue)
}

class ObservableFluxValue<T> constructor(private val flux: Flux<T>, initialValue: T) : ObservableValue<T> {

    private var disposable: Disposable
    private val property = SimpleObjectProperty(initialValue)

    init {
        disposable = flux.subscribe(property::set)
    }

    override fun removeListener(listener: ChangeListener<in T>) {
        property.removeListener(listener)
    }

    override fun removeListener(listener: InvalidationListener) {
        property.removeListener(listener)
    }

    override fun addListener(listener: ChangeListener<in T>) {
        property.addListener(listener)
    }

    override fun addListener(listener: InvalidationListener) {
        property.addListener(listener)
    }

    override fun getValue(): T = property.value

    fun subscribe() {
        if (disposable.isDisposed) {
            disposable = flux.subscribe(property::set)
        }
    }

    fun dispose() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}

fun <T> Flux<T>.emitTo(sink: FluxSink<T>): Flux<T> {
    return doOnComplete { sink.complete() }
        .doOnError { sink.error(it) }
        .doOnNext { sink.next(it) }
}

object JavaFxScheduler {

    private val platform = Schedulers.fromExecutor(Platform::runLater)

    fun platform(): Scheduler {
        return this.platform
    }
}
