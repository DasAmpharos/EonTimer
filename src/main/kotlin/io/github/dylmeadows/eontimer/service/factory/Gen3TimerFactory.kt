package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.factory.timer.FixedFrameTimer
import io.github.dylmeadows.eontimer.service.factory.timer.VariableFrameTimer
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen3TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen3TimerModel: Gen3TimerModel,
    private val fixedFrameTimer: FixedFrameTimer,
    private val variableFrameTimer: VariableFrameTimer) : TimerFactory {

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
                    timerModel.stages = stages
                }
            }
    }

    override val stages: List<Duration>
        get() {
            return when (gen3TimerModel.mode) {
                Gen3TimerMode.STANDARD ->
                    fixedFrameTimer.createStages(
                        gen3TimerModel.preTimer,
                        gen3TimerModel.targetFrame,
                        gen3TimerModel.calibration)
                Gen3TimerMode.VARIABLE_TARGET ->
                    variableFrameTimer.createStages(
                        gen3TimerModel.preTimer)
            }
        }

    override fun createTimer(): Flux<TimerState> {
        return when (gen3TimerModel.mode) {
            Gen3TimerMode.STANDARD ->
                fixedFrameTimer.createTimer(
                    gen3TimerModel.preTimer,
                    gen3TimerModel.targetFrame,
                    gen3TimerModel.calibration)
            Gen3TimerMode.VARIABLE_TARGET ->
                variableFrameTimer.createTimer(
                    gen3TimerModel.preTimer,
                    gen3TimerModel.calibration)
        }
    }

    override fun calibrate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}