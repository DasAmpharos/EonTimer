package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.util.reactor.TimerState
import reactor.core.publisher.Flux
import java.time.Duration

interface TimerFactory {

    val stages: List<Duration>

    fun createTimer(): Flux<TimerState>

    fun calibrate()
}