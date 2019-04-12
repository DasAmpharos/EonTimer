package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Duration

object FluxFactory {

    fun timer(period: Duration, vararg durations: Duration = arrayOf(INDEFINITE)): Flux<TimerState> {
        return Flux.concat(durations.map { duration ->
            timeInterval(period)
                .scan(TimerState(duration = duration), TimerState::plus)
                .takeUntil { state ->
                    state.remaining <= Duration.ZERO
                }
        })
    }

    fun timeInterval(period: Duration): Flux<Duration> {
        return Flux.interval(period).timeInterval()
    }

    fun <T> fromCoroutine(block: suspend CoroutineScope.(FluxSink<T>) -> Unit): Flux<T> {
        return fromCoroutine(GlobalScope, block)
    }

    fun <T> fromCoroutine(scope: CoroutineScope, block: suspend CoroutineScope.(FluxSink<T>) -> Unit): Flux<T> {
        return Flux.create { emitter ->
            val actualBlock: suspend CoroutineScope.() -> Unit = { block(this, emitter) }
            val job = scope.launch(block = actualBlock)
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }
}

fun main() {
    val sub = FluxFactory
        .fromCoroutine<String> { emitter ->
            delay(3000)
            emitter.next("abc")
            emitter.complete()
        }
        .subscribe { println(it) }
    while (!sub.isDisposed) {
    }
}