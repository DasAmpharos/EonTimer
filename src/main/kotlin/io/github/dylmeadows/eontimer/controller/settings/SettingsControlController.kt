package io.github.dylmeadows.eontimer.controller.settings

import javafx.fxml.FXML
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SettingsControlController @Autowired constructor(
    private val actionSettingsController: ActionSettingsController,
    private val timerSettingsController: TimerSettingsController) {

    @FXML
    private fun resetDefaultValues() {
        actionSettingsController.resetDefaultValues()
        timerSettingsController.resetDefaultValues()
    }

    @FXML
    private fun onOkBtnAction() {

    }

    fun show() {
    }
}