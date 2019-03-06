package io.github.dylmeadows.eontimer.controller.settings

import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.springboot.javafx.fxml.SpringFxmlLoader
import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.TabPane
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SettingsDialogController @Autowired constructor(
    loader: SpringFxmlLoader) {

    private lateinit var dialog: Dialog<Unit>

    init {
        Platform.runLater {
            dialog = Dialog()
            // TODO: set title
            val pane = loader.load<Parent>(FxmlResource.SettingsControlPane.get())
            dialog.dialogPane.buttonTypes.setAll(ButtonType.OK)
            dialog.dialogPane.content = pane
        }
    }

    fun showAndWait() {
        dialog.showAndWait()
    }
}