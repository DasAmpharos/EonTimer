package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.springboot.javafx.fxml.SpringFxmlLoader
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SettingsDialog @Autowired constructor(
    private val properties: AppProperties,
    loader: SpringFxmlLoader) {

    private val settingsControlPane = loader.load<Parent>(FxmlResource.SettingsControlPane.get())

    fun showAndWait() {
        val dialog = Dialog<Void>()
        dialog.title = properties.fullApplicationName
        dialog.dialogPane.content = settingsControlPane
        dialog.dialogPane.buttonTypes.setAll(ButtonType.OK)
        dialog.showAndWait()
    }
}