package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.util.milliseconds
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class FixedFrameTimerFactory @Autowired constructor(
    private val calibrationService: CalibrationService) {

    fun createStages(preTimer: Long, targetFrame: Long, calibration: Long): List<Duration> {
        return listOf(stage1(preTimer), stage2(targetFrame, calibration))
    }

    private fun stage1(preTimer: Long): Duration =
        preTimer.milliseconds

    private fun stage2(targetFrame: Long, calibration: Long): Duration {
        return (calibrationService.toMillis(targetFrame) + calibration)
            .milliseconds
    }

    fun calibrate(targetFrame: Long, frameHit: Long): Long =
        calibrationService.toMillis(targetFrame - frameHit)
}