package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.service.Timers
import io.github.dylmeadows.eontimer.util.INDEFINITE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Duration
import java.time.Instant
import java.util.*

object FluxFactory {

    fun fixedTimer(period: Duration, duration: Duration): Flux<TimerState> {
        return fixedTimer(period, duration, Duration.ZERO)
    }

    fun fixedTimer(period: Duration, duration: Duration, preElapsed: Duration): Flux<TimerState> {
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                Timers.fixedTimer(period, duration, preElapsed) {
                    emitter.next(TimerState(it, duration))
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun variableTimer(period: Duration, preElapsed: Duration, takeUntil: (Duration) -> Boolean): Flux<TimerState> {
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                Timers.variableTimer(period, preElapsed,
                    { emitter.next(TimerState(it, INDEFINITE)) },
                    takeUntil)
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun timeInterval(period: Duration): Flux<Duration> {
        return Flux.interval(period).timeInterval()
    }
}