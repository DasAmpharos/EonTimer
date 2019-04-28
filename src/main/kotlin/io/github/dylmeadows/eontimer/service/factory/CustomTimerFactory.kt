package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class CustomTimerFactory @Autowired constructor(
    private val timerState: TimerState,
    private val customTimerModel: CustomTimerModel) : TimerFactory {

    override val stages: List<Duration>
        get() = customTimerModel.stages
            .map { Duration.ofMillis(it.length) }

    @PostConstruct
    private fun initialize() {
        customTimerModel.stages.asFlux()
            .subscribe {
                timerState.update(stages)
            }
    }

    override fun calibrate() = Unit
}