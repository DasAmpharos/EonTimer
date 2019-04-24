package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.getStage
import io.github.dylmeadows.eontimer.util.sum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class TimerFactoryService @Autowired constructor(
    private val timerState: TimerState,
    private val gen3TimerFactory: TimerFactory,
    private val gen4TimerFactory: TimerFactory,
    private val gen5TimerFactory: TimerFactory,
    private val customTimerFactory: TimerFactory,
    private val applicationModel: ApplicationModel) {

    val stages: List<Duration> get() = timerFactory.stages
    private val timerFactory: TimerFactory get() = applicationModel.selectedTimerType.timerFactory

    @PostConstruct
    private fun initialize() {
        applicationModel.selectedTimerTypeProperty.asFlux()
            .map { it.timerFactory }
            .map(TimerFactory::stages)
            .subscribe {
                timerState.currentStage = it.getStage(0)
                timerState.nextStage = it.getStage(1)
                timerState.totalTime = it.sum()
            }
    }

    private val TimerType.timerFactory: TimerFactory
        get() = when (this) {
            TimerType.GEN3 -> gen3TimerFactory
            TimerType.GEN4 -> gen4TimerFactory
            TimerType.GEN5 -> gen5TimerFactory
            TimerType.CUSTOM -> customTimerFactory
        }

    fun calibrate() = applicationModel.selectedTimerType.timerFactory.calibrate()
}