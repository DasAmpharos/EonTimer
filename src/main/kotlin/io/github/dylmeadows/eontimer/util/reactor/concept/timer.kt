package io.github.dylmeadows.eontimer.util.reactor.concept

import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.delay
import io.github.dylmeadows.eontimer.util.seconds
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.Instant
import java.util.concurrent.CountDownLatch

suspend fun fixedTimer(period: Duration, duration: Duration, onTick: (Duration) -> Unit): Duration {
    return fixedTimer(period, duration, Duration.ZERO, onTick)
}

suspend fun fixedTimer(period: Duration, duration: Duration, preElapsed: Duration, onTick: (Duration) -> Unit): Duration {
    var elapsed = preElapsed
    var adjustedDelay = period
    var lastTimestamp = Instant.now()
    while (elapsed < duration) {
        delay(adjustedDelay.toMillis())

        val now = Instant.now()
        val delta = Duration.between(lastTimestamp, now)
        if ((duration - elapsed) < period) {
            adjustedDelay = duration - elapsed
        } else {
            adjustedDelay -= delta - period
        }
        lastTimestamp = now
        elapsed += delta
        onTick(elapsed)
    }
    return elapsed
}

suspend fun variableTimer(period: Duration, preElapsed: Duration, onTick: (Duration) -> Unit, takeUntil: (Duration) -> Boolean): Duration {
    var elapsed = preElapsed
    var adjustedDelay = period
    var lastTimestamp = Instant.now()
    while (!takeUntil(elapsed)) {
        delay(adjustedDelay.toMillis())

        val now = Instant.now()
        val delta = Duration.between(lastTimestamp, now)
        adjustedDelay -= delta - period
        lastTimestamp = now
        elapsed += delta
        onTick(elapsed)
    }
    return elapsed
}

fun test(): Flux<Duration> {
    var targetFrame = -1L
    val period = 10L.milliseconds
    return Flux.create { emitter ->
        val job = GlobalScope.launch {
            var elapsed = fixedTimer(period, 5L.seconds) { emitter.next(it) }
            launch {
                10L.seconds.delay()
                targetFrame = 1
            }
            elapsed = variableTimer(period, elapsed - 5L.seconds,
                { emitter.next(it) }, { targetFrame != -1L })
            fixedTimer(period, 15L.seconds, elapsed) { emitter.next(it) }
            emitter.complete()
        }
        emitter.onDispose(job::cancel)
        emitter.onCancel(job::cancel)
    }
}

fun main() {
    val latch = CountDownLatch(1)
    test().doOnComplete(latch::countDown)
        .subscribe { println(it.toMillis()) }
    val start = Instant.now()
    latch.await()
    val stop = Instant.now()
    println(Duration.between(start, stop))
}