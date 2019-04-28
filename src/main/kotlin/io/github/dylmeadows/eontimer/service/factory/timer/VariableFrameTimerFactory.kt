package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.util.INDEFINITE
import io.github.dylmeadows.eontimer.util.milliseconds
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class VariableFrameTimerFactory {

    fun createStages(preTimer: Long): List<Duration> {
        return listOf(preTimer.milliseconds, INDEFINITE)
    }
}