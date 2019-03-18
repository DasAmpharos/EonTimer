package io.github.dylmeadows.eontimer.service.factory

import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.TimerModel
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.util.asFlux
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TimerFactoryService @Autowired constructor(
    private val timerModel: TimerModel,
    private val gen3TimerFactory: TimerFactory,
    private val gen4TimerFactory: TimerFactory,
    private val gen5TimerFactory: TimerFactory,
    private val customTimerFactory: TimerFactory,
    private val applicationModel: ApplicationModel) {

    @PostConstruct
    private fun initialize() {
        applicationModel.selectedTimerTypeProperty.asFlux()
            .map { it.timerFactory }
            .map(TimerFactory::createTimer)
            .subscribe { timerModel.stages = it }
    }

    private val TimerType.timerFactory: TimerFactory
        get() = when (this) {
            TimerType.GEN3 -> gen3TimerFactory
            TimerType.GEN4 -> gen4TimerFactory
            TimerType.GEN5 -> gen5TimerFactory
            TimerType.CUSTOM -> customTimerFactory
        }
}