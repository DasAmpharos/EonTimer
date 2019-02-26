package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomTimerFactory @Autowired constructor(
    private val model: CustomTimerModel) : TimerFactory {

    override fun createTimer(): List<Long> {
        return model.stages.toUnmodifiableList()
    }
}