package io.github.dylmeadows.eontimer.controller.timer

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.asIntField
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomTimerPaneController @Autowired constructor(
    private val model: CustomTimerModel) {

    @FXML
    private lateinit var list: ListView<Long>
    @FXML
    private lateinit var valueField: TextField
    @FXML
    private lateinit var valueAddBtn: Button
    @FXML
    private lateinit var valueRemoveBtn: Button
    @FXML
    private lateinit var valueMoveUpBtn: Button
    @FXML
    private lateinit var valueMoveDownBtn: Button

    fun initialize() {
        list.items = model.stages
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE

        val valueIntField = valueField.asIntField()
        valueField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                add(valueIntField.value)
                valueField.text = ""
            }
        }
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty().bind(valueField.textProperty().isEmpty)
        valueAddBtn.setOnAction {
            add(valueIntField.value)
            valueField.text = ""
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty().bind(list.selectionModel.selectedItemProperty().isNull)
        valueRemoveBtn.setOnAction {
            removeAll(list.selectionModel.selectedIndices
                .map { model.stages[it] }
                .toList())
        }

        valueMoveUpBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_UP)
        valueMoveDownBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_DOWN)
    }

    private fun add(value: Int) {
        model.stages.add(value.toLong())
    }

    private fun remove(value: Long) {
        model.stages.remove(value)
    }

    private fun removeAll(values: List<Long>) {
        model.stages.removeAll(values)
    }
}