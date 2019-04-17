package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class CustomTimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val timerSettings: TimerSettingsModel,
    private val customTimerModel: CustomTimerModel) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        customTimerModel.stages.asFlux()
            .map { stages -> stages
                .map { it.length }
                .map(Duration::ofMillis)
            }
            .subscribe { timerModel.stages = it }
    }

    override val stages: List<Duration>
        get() = customTimerModel.stages.map { it.length }
            .map(Duration::ofMillis)

    override fun createTimer(): Flux<TimerState> {
        return FluxFactory.fixedTimer(timerSettings.refreshInterval.milliseconds, stages)
    }

    override fun calibrate() = Unit
}