package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.service.Timers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import java.time.Duration

object FluxFactory {

    fun fixedTimer(period: Duration, duration: Duration): Flux<TimerTick> {
        return fixedTimer(period, duration, Duration.ZERO)
    }

    fun fixedTimer(period: Duration, duration: Duration, preElapsed: Duration): Flux<TimerTick> {
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                Timers.fixedTimer(period, duration, preElapsed) { tick ->
                    emitter.next(tick)
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun fixedTimer(period: Duration, durations: List<Duration>): Flux<TimerTick> {
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                var preElapsed = Duration.ZERO
                durations.forEach { duration ->
                    preElapsed = Timers.fixedTimer(period, duration, preElapsed) { tick ->
                        emitter.next(tick)
                    }
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    fun variableTimer(period: Duration, preElapsed: Duration, takeUntil: (Duration) -> Boolean): Flux<TimerTick> {
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                Timers.variableTimer(period, preElapsed,
                    { emitter.next(it) },
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