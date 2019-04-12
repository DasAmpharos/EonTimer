package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.transform
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
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
        return customTimerModel.stages.map { it.length }
            .transform(Collections::unmodifiableList)
    }

    override fun calibrate() = Unit
}