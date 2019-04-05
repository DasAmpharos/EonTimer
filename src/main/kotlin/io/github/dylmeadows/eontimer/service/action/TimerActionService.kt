package io.github.dylmeadows.eontimer.service.action

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.*
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.annotation.PostConstruct

@Service
class TimerActionService @Autowired constructor(
    private val timerState: TimerState,
    private val timerActionSettingsModel: ActionSettingsModel,
    private val soundPlayer: SoundPlayer) {

    val activeProperty: BooleanProperty = SimpleBooleanProperty(false)
    private var active by activeProperty

    @PostConstruct
    private fun initialize() {
        val actionIntervalProperty = anyChangesOf(timerActionSettingsModel.countProperty,
            timerActionSettingsModel.intervalProperty)
            .map { it.mapT1(Number::toInt) }
            .map { it.mapT2(Number::toInt) }
            .map { createActionInterval(it.t1, it.t2) }
            .asObservableValue(Collections.emptyList())

        var actionIndex = 0
        anyChangesOf(timerState.runningProperty, actionIntervalProperty, timerState.remainingProperty)
            .map { it.mapT3(Number::toLong) }
            .map { ActionState(it.t1, it.t2, it.t3) }
            .subscribe {
                if (it.running && it.remaining <= it.actionInterval[actionIndex]) {
                    actionIndex = getNextActionIndex(actionIndex, it.actionInterval)
                    invokeAction()
                }
            }

        timerState.runningProperty.asFlux()
            .subscribe { actionIndex = 0 }
    }

    private fun createActionInterval(count: Int, interval: Int): List<Int> {
        return IntRange(0, count - 1)
            .toList().asReversed()
            .map { it * interval }
    }

    private fun getNextActionIndex(actionIndex: Int, actionInterval: List<Int>): Int {
        return if (actionIndex + 1 < actionInterval.size) actionIndex + 1 else 0
    }

    private fun invokeAction() {
        if (timerActionSettingsModel.mode == ActionMode.AUDIO || timerActionSettingsModel.mode == ActionMode.AV)
            soundPlayer.play()
        if (timerActionSettingsModel.mode == ActionMode.VISUAL || timerActionSettingsModel.mode == ActionMode.AV) {
            active = true
            GlobalScope.launch {
                delay(75)
                active = false
            }
        }
    }

    private data class ActionState(
        val running: Boolean,
        val actionInterval: List<Int>,
        val remaining: Long)
}