package io.github.dylmeadows.eontimer.service.lifecycle

import io.github.dylmeadows.eontimer.model.TimerState

typealias TimerLifecycleListener = (TimerState) -> Unit