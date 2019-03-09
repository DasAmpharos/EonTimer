package io.github.dylmeadows.eontimer.controller.timer

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.createValueFactory
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.input.KeyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomTimerPaneController @Autowired constructor(
    private val model: CustomTimerModel,
    private val timerService: TimerService) : TimerPaneController {

    @FXML
    private lateinit var list: ListView<Long>
    @FXML
    private lateinit var valueField: Spinner<Int>
    @FXML
    private lateinit var valueAddBtn: Button
    @FXML
    private lateinit var valueRemoveBtn: Button
    @FXML
    private lateinit var valueMoveUpBtn: Button
    @FXML
    private lateinit var valueMoveDownBtn: Button

    override val canUpdate: BooleanBinding
        get() = timerService.runningProperty.not()

    fun initialize() {
        list.items = model.stages
        list.selectionModel.selectionMode = SelectionMode.MULTIPLE

        valueField.createValueFactory(0, Integer.MAX_VALUE)
        valueField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                add(valueField.value)
            }
        }

        valueMoveUpBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_UP)
        valueMoveDownBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_DOWN)

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty().bind(
            valueField.editor.textProperty().isEmpty)
        valueAddBtn.setOnAction {
            add(valueField.value)
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty().bind(
            list.selectionModel.selectedItemProperty().isNull)
        valueRemoveBtn.setOnAction {
            removeAll(list.selectionModel.selectedIndices
                .map { model.stages[it] }
                .toList())
        }
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