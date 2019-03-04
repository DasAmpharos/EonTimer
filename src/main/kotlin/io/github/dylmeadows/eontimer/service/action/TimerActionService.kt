package io.github.dylmeadows.eontimer.service.action

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TimerActionService @Autowired constructor(
    private val soundPlayer: SoundPlayer)