package io.github.dylmeadows.eontimer.service.action

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.anyChangesOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class TimerActionService @Autowired constructor(
    private val timerState: TimerState,
    private val timerActionSettingsModel: ActionSettingsModel,
    private val soundPlayer: SoundPlayer) {

    @PostConstruct
    private fun initialize() {
        anyChangesOf(timerActionSettingsModel.countProperty,
            timerActionSettingsModel.intervalProperty,
            ::createActionInterval)
            .subscribe { println(it) }
    }

    private fun createActionInterval(count: Number, interval: Number): List<Int> {
        return IntRange(0, count.toInt() - 1)
            .toList().asReversed()
            .map { it * interval.toInt() }
    }

    private data class ActionState(
        val actionInterval: List<Int>,
        val actionIndex: Int,
        val remaining: Long)
}