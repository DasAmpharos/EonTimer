package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource

enum class FxmlResource(private val path: String) : Resource {
    EonTimerPane("EonTimerPane.fxml"),
    Gen3TimerPane("timer/Gen3TimerPane.fxml"),
    Gen4TimerPane("timer/Gen4TimerPane.fxml"),
    Gen5TimerPane("timer/Gen5TimerPane.fxml"),
    CustomTimerPane("timer/CustomTimerPane.fxml"),
    TimerControlPane("timer/TimerControlPane.fxml"),
    ActionSettingsPane("settings/ActionSettingsPane.fxml"),
    TimerSettingsPane("settings/TimerSettingsPane.fxml"),
    SettingsControlPane("settings/SettingsControlPane.fxml"),
    TimerDisplayPane("TimerDisplayPane.fxml");

    override fun getPath(): String {
        return "$BASE_RESOURCE_PATH/fxml/${this.path}"
    }
}