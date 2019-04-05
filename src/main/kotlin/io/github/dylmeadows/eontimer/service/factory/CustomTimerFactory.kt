package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.collections.toUnmodifiableList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class CustomTimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val customTimerModel: CustomTimerModel) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        customTimerModel.stages.asFlux()
            .map { stages -> stages.map { it.length } }
            .subscribe { timerModel.stages = it }
    }

    override fun createTimer(): List<Long> {
        return customTimerModel.stages
            .map { it.length }
            .toUnmodifiableList()
    }

    override fun calibrate() = Unit
}