package io.github.dylmeadows.eontimer.service.action

import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.anyChangesOf
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class TimerActionService @Autowired constructor(
    private val timerActionSettingsModel: ActionSettingsModel,
    private val soundPlayer: SoundPlayer) {

    var actionInterval: List<Duration> = Collections.emptyList()
        private set

    val activeProperty: BooleanProperty = SimpleBooleanProperty(false)
    private var active by activeProperty

    init {
        anyChangesOf(
            timerActionSettingsModel.countProperty,
            timerActionSettingsModel.intervalProperty)
            .map { it.mapT1(Number::toInt) }
            .map { it.mapT2(Number::toInt) }
            .map { createActionInterval(it.t1, it.t2) }
            .subscribe { actionInterval = it }
    }

    private fun createActionInterval(count: Int, interval: Int): List<Duration> {
        return IntRange(0, count - 1)
            .toList().asReversed()
            .map { it * interval }
            .map(Number::toLong)
            .map(Duration::ofMillis)
    }

    fun invokeAction() {
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
}