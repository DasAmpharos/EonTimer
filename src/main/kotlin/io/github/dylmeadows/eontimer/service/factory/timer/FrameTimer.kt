package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.service.CalibrationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FrameTimer @Autowired constructor(
    private val calibrationService: CalibrationService) {

    fun createStages(calibration: Long, preTimer: Long, targetFrame: Long): List<Long> {
        return listOf(
            preTimer,
            createStage2(calibration, targetFrame))
    }

    private fun createStage2(calibration: Long, targetFrame: Long): Long {
        return calibrationService.toMillis(targetFrame) + calibration
    }
}