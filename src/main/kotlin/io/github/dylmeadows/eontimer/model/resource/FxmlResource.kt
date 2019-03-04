package io.github.dylmeadows.eontimer.model.resource

import io.github.dylmeadows.common.core.io.Resource

enum class FxmlResource(private val path: String) : Resource {
    Gen3TimerPane("$BASE_RESOURCE_PATH/fxml/timer/Gen3TimerPane.fxml"),
    Gen4TimerPane("$BASE_RESOURCE_PATH/fxml/timer/Gen4TimerPane.fxml"),
    Gen5TimerPane("$BASE_RESOURCE_PATH/fxml/timer/Gen5TimerPane.fxml"),
    CustomTimerPane("$BASE_RESOURCE_PATH/fxml/timer/CustomTimer.fxml"),
    ActionSettingsPane("$BASE_RESOURCE_PATH/fxml/settings/ActionSettingsPane.fxml"),
    TimerDisplayPane("$BASE_RESOURCE_PATH/fxml/TimerDisplayPane.fxml"),
    TimerControlPane("$BASE_RESOURCE_PATH/fxml/TimerControlPane.fxml");

    override fun getPath(): String {
        return this.path
    }
}