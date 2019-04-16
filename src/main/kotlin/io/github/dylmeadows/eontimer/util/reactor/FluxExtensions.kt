package io.github.dylmeadows.eontimer.util.reactor

import io.github.dylmeadows.eontimer.util.addDisposable
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.seconds
import kotlinx.coroutines.delay
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Duration
import java.time.Instant
import java.util.concurrent.CountDownLatch
import java.util.function.Consumer

fun Flux<Long>.timeInterval(): Flux<Duration> {
    return elapsed()
        .map { it.t1 }
        .map(Duration::ofMillis)
}

suspend fun FluxSink<TimerState>.asTimer(period: Duration, duration: Duration): Duration {
    var delayDuration = period
    var elapsed = Duration.ZERO
    var lastTimestamp = Instant.now()
    while (elapsed < duration) {
        delay(delayDuration.toMillis())

        val now = Instant.now()
        val delta = Duration.between(lastTimestamp, now)
        // adjust delay
        if ((duration - elapsed) < period) {
            delayDuration = duration - elapsed
        } else {
            delayDuration -= delta - period
        }
        lastTimestamp = now
        elapsed += delta

        next(TimerState(delta, elapsed, duration))
    }
    elapsed -= duration
    return elapsed
}

suspend fun FluxSink<TimerState>.asTimer(period: Duration, durations: List<Duration>): Duration {
    return durations
        .map { asTimer(period, it) }
        .reduce(Duration::plus)
}

suspend fun timer(period: Duration, duration: Duration, emitter: FluxSink<Duration>): Duration {
    return timer(period, duration, Duration.ZERO, emitter)
}

suspend fun timer(period: Duration, duration: Duration, preElapsed: Duration, emitter: FluxSink<Duration>): Duration {
    var elapsed = preElapsed
    var adjustedPeriod = period
    var lastTimestamp = Instant.now()
    while (elapsed < duration) {
        adjustedPeriod.delay()

        val now = Instant.now()
        val delta = Duration.between(lastTimestamp, now)
        // adjust delay
        if ((duration - elapsed) < period) {
            adjustedPeriod = duration - elapsed
        } else {
            adjustedPeriod -= delta - period
        }
        lastTimestamp = now
        elapsed += delta

        emitter.next(elapsed)
    }
    return elapsed
}

suspend fun timerWhile(period: Duration, emitter: FluxSink<Duration>, predicate: (Duration) -> Boolean): Duration {
    return timerWhile(period, Duration.ZERO, emitter, predicate)
}

suspend fun timerWhile(period: Duration, preElapsed: Duration, emitter: FluxSink<Duration>, predicate: (Duration) -> Boolean): Duration {
    var elapsed = preElapsed
    var adjustedPeriod = period
    var lastTimestamp = Instant.now()
    while (predicate(elapsed)) {
        adjustedPeriod.delay()

        val now = Instant.now()
        val delta = Duration.between(lastTimestamp, now)
        adjustedPeriod -= delta - period
        lastTimestamp = now
        elapsed += delta

        emitter.next(elapsed)
    }
    return elapsed
}

//suspend fun timerUntil(period: Duration, emitter: FluxSink<Duration>, predicate: (Duration) -> Boolean): Duration {
//}

suspend fun Duration.delay() {
    delay(toMillis())
}

suspend fun main() {
    var targetFrame = -1L
    val latch = CountDownLatch(1)

    val flux = Flux.create<Duration> { emitter ->
        val disposable = timer(1L.seconds, 10L.seconds)
            .chainWithLast { lastElapsed ->
                timer(1L.seconds, lastElapsed - 10L.seconds) {
                    targetFrame == -1L
                }
            }
            .chainWithLast { lastElapsed ->
                timer(1L.seconds, 5L.seconds, lastElapsed)
            }
            .doOnNext { emitter.next(it) }
            .doOnComplete(emitter::complete)
            .subscribe()
        emitter.addDisposable(disposable)
    }
    flux.doOnComplete(latch::countDown)
        .doOnNext { println(it) }
        .subscribe()

    12L.seconds.delay()
    targetFrame = 1

    latch.await()
}

fun <T> Flux<T>.chainWithLast(mapper: (T) -> Flux<T>): Flux<T> {
    return last().flatMapMany(mapper)
}

fun timer(period: Duration, duration: Duration): Flux<Duration> {
    return timer(period) { elapsed -> elapsed < duration }
}

fun timer(period: Duration, duration: Duration, preElapsed: Duration): Flux<Duration> {
    return timer(period, preElapsed) { elapsed -> elapsed < duration }
}

fun timer(period: Duration, predicate: (Duration) -> Boolean): Flux<Duration> {
    return timer(period, Duration.ZERO, predicate)
}

fun timer(period: Duration, preElapsed: Duration, predicate: (Duration) -> Boolean): Flux<Duration> {
    return Flux.interval(period)
        .timeInterval()
        .scan(preElapsed, Duration::plus)
        .takeWhile(predicate)
}