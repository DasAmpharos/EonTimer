package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.milliseconds
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.eontimer.util.reactor.TimerState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.Duration

@Service
class FixedFrameTimer @Autowired constructor(
    private val calibrationService: CalibrationService) {

    fun createStages(preTimer: Long, targetFrame: Long, calibration: Long): List<Long> {
        return listOf(
            preTimer,
            createStage2(targetFrame, calibration))
    }

    private fun createStage2(targetFrame: Long, calibration: Long): Long {
        return calibrationService.toMillis(targetFrame) + calibration
    }

    fun start(preTimer: Long, targetFrame: Long, calibration: Long): Flux<TimerState> {
        val stages = createStages(preTimer, targetFrame, calibration).map(Duration::ofMillis)
        return FluxFactory.multiTimer(8L.milliseconds, stages)
    }
}