package io.github.dylmeadows.eontimer.util

import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import reactor.core.publisher.Flux
import reactor.core.scheduler.Scheduler
import reactor.core.scheduler.Schedulers

fun <T> ObservableValue<T>.asFlux(): Flux<T> {
    return Flux.create { emitter ->
        emitter.next(value)
        val listener: ChangeListener<T> = ChangeListener { _, _, newValue ->
            emitter.next(newValue)
        }
        this.addListener(listener)

        emitter.onDispose {
            Platform.runLater {
                this.removeListener(listener)
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
