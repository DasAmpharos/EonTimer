package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.util.Calibrations.convertToMillis
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class Gen3TimerFactory @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen3TimerModel: Gen3TimerModel,
    private val timerSettings: TimerSettingsModel) : TimerFactory {

    @PostConstruct
    private fun initialize() {
        gen3TimerModel.modeProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen3TimerModel.calibrationProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen3TimerModel.preTimerProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
        gen3TimerModel.targetFrameProperty.asFlux()
            .subscribe { timerModel.stages = createTimer() }
    }

    override fun createTimer(): List<Long> {
        return when (gen3TimerModel.mode) {
            Gen3TimerMode.STANDARD ->
                longArrayOf(
                    createStage1(),
                    createStage2())
                    .toList()
            Gen3TimerMode.VARIABLE_TARGET ->
                // TODO: implement this
                emptyList()
        }
    }

    private fun createStage1(): Long {
        return gen3TimerModel.preTimer.toLong()
    }

    private fun createStage2(): Long {
        return (convertToMillis(gen3TimerModel.targetFrame, timerSettings.console) + gen3TimerModel.calibration).toLong()
    }

}