package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.springboot.javafx.fxml.SpringFxmlLoader
import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SettingsDialog @Autowired constructor(
    private val properties: AppProperties,
    loader: SpringFxmlLoader) {

    private lateinit var dialog: Dialog<Unit>

    init {
        Platform.runLater {
            dialog = Dialog()
            dialog.title = properties.fullApplicationName
            dialog.dialogPane.content = loader.load(FxmlResource.SettingsControlPane.get())
            dialog.dialogPane.buttonTypes.setAll(ButtonType.OK)
        }
    }

    fun showAndWait() {
        dialog.showAndWait()
    }
}