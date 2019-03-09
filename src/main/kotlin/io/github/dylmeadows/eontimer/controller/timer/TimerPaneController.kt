package io.github.dylmeadows.eontimer.controller.timer

import javafx.beans.binding.BooleanBinding

interface TimerPaneController {
    val canUpdate: BooleanBinding
}