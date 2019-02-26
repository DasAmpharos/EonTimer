package io.github.dylmeadows.eontimer.service

import io.github.dylmeadows.common.core.util.Option
import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.TimerConstants
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.util.changesAsFlux
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tornadofx.getValue
import javax.annotation.PostConstruct

@Service
class TimerService @Autowired constructor(
    private val model: TimerModel,
    private val settings: TimerSettingsModel) {

    private lateinit var timerJob: Job

    val stateProperty = SimpleObjectProperty<TimerState>()
    val state by stateProperty
    val runningProperty = SimpleBooleanProperty(false)
    val running by runningProperty

    @PostConstruct
    private fun initialize() {
        model.stagesProperty
            .changesAsFlux<List<Long>>()
            // .doOnNext { emitInit() }
            .subscribe()
    }

    fun start() {
        timerJob = GlobalScope.launch(Dispatchers.JavaFx) {
            var overlap = 0L
            model.stages
                .forEach {
                    var remaining = it + overlap
                    var lastTimestamp = System.currentTimeMillis()
                    while (remaining > 0) {
                        val now = System.currentTimeMillis()
                        val delta = now - lastTimestamp
                        remaining -= delta
                        stateProperty.value = TimerState(0, 0, remaining, 0)
                        lastTimestamp = now
                        delay(settings.refreshInterval.toLong())
                    }
                    overlap = remaining
                }
        }
    }

    fun stop() {
        timerJob.cancel()
    }

    private fun getStage(stages: List<Long>, index: Int): Long {
        return Option.of(stages)
            .filter { it.isNotEmpty() }
            .map { list -> list[index] }
            .orElse(TimerConstants.NULL_TIME_SPAN)
    }
}