package io.github.dylmeadows.eontimer.service.factory.timer

import io.github.dylmeadows.eontimer.model.timer.Gen5TimerConstants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EnhancedEntralinkTimer @Autowired constructor(
    private val entralinkTimer: EntralinkTimer) {

    fun createStages(calibration: Long, entralinkCalibration: Long, frameCalibration: Long, targetSecond: Long, targetDelay: Long, targetAdvances: Long): List<Long> {
        val stages = entralinkTimer.createStages(calibration, entralinkCalibration, targetSecond, targetDelay)
        return listOf(stages[0], stages[1],
            createStage3(targetAdvances, frameCalibration))
    }

    fun calibrate(targetAdvances: Long, actualAdvances: Long): Long {
        return Math.round((targetAdvances - actualAdvances) / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000

        // TODO: 4/1/19 - determine if this is still needed
        // val npcRate = 1.0 / calibrationService.toMillis(32)
        // return Math.round((targetAdvances - actualAdvances) / (Gen5TimerConstants.ENTRALINK_FRAME_RATE + (npcCount * npcRate))) * 1000
    }

    private fun createStage3(targetAdvances: Long, frameCalibration: Long): Long {
        return Math.round(targetAdvances / Gen5TimerConstants.ENTRALINK_FRAME_RATE) * 1000 + frameCalibration
    }
}