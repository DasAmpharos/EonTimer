package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.service.factory.timer.FixedFrameTimerFactory
import io.github.dylmeadows.eontimer.service.factory.timer.VariableFrameTimerFactory
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class Gen3TimerFactory @Autowired constructor(
    private val timerState: TimerState,
    private val gen3TimerModel: Gen3TimerModel,
    private val fixedFrameTimerFactory: FixedFrameTimerFactory,
    private val variableFrameTimerFactory: VariableFrameTimerFactory) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        listOf(
            gen3TimerModel.modeProperty,
            gen3TimerModel.preTimerProperty,
            gen3TimerModel.calibrationProperty)
            .map { it.asFlux() }
            .forEach {
                it.subscribe {
                    timerState.update(stages)
                }
            }
        gen3TimerModel.targetFrameProperty.asFlux()
            .filter { gen3TimerModel.mode == Gen3TimerMode.STANDARD }
            .subscribe { timerState.update(stages) }
    }

    override val stages: List<Duration>
        get() {
            return when (gen3TimerModel.mode) {
                Gen3TimerMode.STANDARD ->
                    fixedFrameTimerFactory.createStages(
                        gen3TimerModel.preTimer,
                        gen3TimerModel.targetFrame,
                        gen3TimerModel.calibration)
                Gen3TimerMode.VARIABLE_TARGET ->
                    variableFrameTimerFactory.createStages(
                        gen3TimerModel.preTimer)
            }
        }

    override fun calibrate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}