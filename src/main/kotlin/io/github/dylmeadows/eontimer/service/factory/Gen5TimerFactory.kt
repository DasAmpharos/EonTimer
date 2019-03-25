package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Gen5TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen5TimerModel: Gen5TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    private val stages: LongArray
        get() {
            return longArrayOf()
        }

    override fun createTimer(): List<Long> {
        return stages.toList()
    }
}