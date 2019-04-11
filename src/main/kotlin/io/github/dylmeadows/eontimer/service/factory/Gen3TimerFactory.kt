package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.factory.timer.FixedFrameTimer
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Gen3TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen3TimerModel: Gen3TimerModel,
    private val fixedFrameTimer: FixedFrameTimer) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        listOf(
            gen3TimerModel.modeProperty,
            gen3TimerModel.calibrationProperty,
            gen3TimerModel.preTimerProperty,
            gen3TimerModel.targetFrameProperty)
            .map { it.asFlux() }
            .forEach {
                it.subscribe {
                    timerModel.stages = createTimer()
                }
            }
    }

    override fun createTimer(): List<Long> {
        return when (gen3TimerModel.mode) {
            Gen3TimerMode.STANDARD ->
                fixedFrameTimer.createStages(
                    gen3TimerModel.calibration,
                    gen3TimerModel.preTimer,
                    gen3TimerModel.targetFrame)
            Gen3TimerMode.VARIABLE_TARGET ->
                // TODO: implement this
                emptyList()
        }
    }

    override fun calibrate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}