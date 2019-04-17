package io.github.dylmeadows.eontimer.service.factory.timer.concept

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.service.Timers
import io.github.dylmeadows.eontimer.util.NULL
import io.github.dylmeadows.eontimer.util.milliseconds
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import reactor.core.publisher.Flux
import java.time.Duration

class FixedTimer constructor(
    val stages: List<Duration>) {

    fun start(period: Duration): Flux<TimerState> {
        val totalDuration = stages
            .map(Duration::toMillis)
            .sum()
        val state = TimerState(Duration.ZERO, totalDuration.milliseconds, )
        return Flux.create { emitter ->
            val job = GlobalScope.launch {
                var preElapsed = Duration.ZERO
                stages.forEachIndexed { index, stage ->
                    preElapsed = Timers.fixedTimer(period, stage, preElapsed) { elapsed ->
                        state.remaining = (stage - elapsed).toMillis()
                        state.nextStage = getStage(index + 1)
                    }
                }
                emitter.complete()
            }
            emitter.onDispose(job::cancel)
            emitter.onCancel(job::cancel)
        }
    }

    private fun getStage(index: Int): Duration {
        return if (index < stages.size) stages[index] else NULL
    }
}