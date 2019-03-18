package io.github.dylmeadows.eontimer.controller

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.github.dylmeadows.eontimer.controller.settings.SettingsDialogController
import javafx.fxml.FXML
import javafx.scene.control.Button
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class EonTimerPaneController @Autowired constructor(
    private val settingsDialog: SettingsDialogController) {

    @FXML
    private lateinit var settingsBtn: Button

    fun initialize() {
        settingsBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.GEAR)
        settingsBtn.setOnAction {
            settingsDialog.showAndWait()
        }
    }
}