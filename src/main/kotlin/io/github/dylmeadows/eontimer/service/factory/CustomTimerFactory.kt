package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class CustomTimerFactory @Autowired constructor(
    private val customTimerModel: CustomTimerModel) : TimerFactory {

    override val stages: List<Duration>
        get() = customTimerModel.stages
            .map { Duration.ofMillis(it.length) }

    override fun calibrate() = Unit
}