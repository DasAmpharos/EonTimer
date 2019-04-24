package io.github.dylmeadows.eontimer.controller.timer

import de.jensd.fx.glyphs.GlyphsDude
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import io.github.dylmeadows.eontimer.model.Stage
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.LongValueFactory
import io.github.dylmeadows.eontimer.util.StageStringConverter
import io.github.dylmeadows.eontimer.util.text
import io.github.dylmeadows.eontimer.util.textProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.Spinner
import javafx.scene.control.cell.TextFieldListCell
import javafx.scene.input.KeyCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomTimerPane @Autowired constructor(
    private val model: CustomTimerModel) {

    @FXML
    private lateinit var list: ListView<Stage>
    @FXML
    private lateinit var valueField: Spinner<Long>
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
        list.cellFactory = TextFieldListCell.forListView(StageStringConverter())

        valueField.valueFactory = LongValueFactory(0L)
        valueField.setOnKeyPressed {
            if (it.code == KeyCode.ENTER) {
                model.stages.add(Stage(valueField.value))
                valueField.text = ""
            }
        }
        valueField.text = ""

        valueAddBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.PLUS)
        valueAddBtn.disableProperty().bind(valueField.textProperty.isEmpty)
        valueAddBtn.setOnAction {
            model.stages.add(Stage(valueField.value))
            valueField.text = ""
        }

        valueRemoveBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.MINUS)
        valueRemoveBtn.disableProperty().bind(list.selectionModel.selectedItemProperty().isNull)
        valueRemoveBtn.setOnAction {
            list.selectionModel.selectedIndices
                .map { model.stages[it] }
                .forEach { model.stages.remove(it) }
        }

        valueMoveUpBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_UP)
        valueMoveDownBtn.graphic = GlyphsDude.createIcon(FontAwesomeIcon.CHEVRON_DOWN)
    }
}