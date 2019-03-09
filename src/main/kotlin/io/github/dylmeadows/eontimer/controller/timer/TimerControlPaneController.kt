package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.JavaFxScheduler
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.log
import javafx.beans.property.ReadOnlyBooleanProperty
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerControlPaneController @Autowired constructor(
    private val model: ApplicationModel,
    private val timerService: TimerService,
    private val gen3Controller: Gen3TimerPaneController,
    private val gen4Controller: Gen4TimerPaneController,
    private val gen5Controller: Gen5TimerPaneController,
    private val customController: CustomTimerPaneController) {

    @FXML
    private lateinit var gen3Tab: Tab
    @FXML
    private lateinit var gen4Tab: Tab
    @FXML
    private lateinit var gen5Tab: Tab
    @FXML
    private lateinit var customTab: Tab
    @FXML
    private lateinit var timerTabPane: TabPane
    @FXML
    private lateinit var updateBtn: Button
    @FXML
    private lateinit var timerBtn: Button

    fun initialize() {
        timerTabPane.selectionModel.selectedItemProperty().asFlux()
            .map { getTimerType(it.newValue) }
            .subscribe({
                model.selectedTimerType = it
                updateUpdateBtnDisableProperty(it)
            }, {
                log.error(it.message, it)
                System.exit(-1)
            })

        timerService.runningProperty.asFlux()
            .subscribeOn(JavaFxScheduler.platform())
            .map { if (!it.newValue) "Start" else "Stop" }
            .subscribe { timerBtn.text = it }
        timerBtn.setOnAction {
            if (!timerService.running) {
                timerService.start()
            } else {
                timerService.stop()
            }
        }
    }

    private fun getTimerType(tab: Tab): TimerType {
        return when (tab) {
            gen3Tab -> TimerType.GEN3
            gen4Tab -> TimerType.GEN4
            gen5Tab -> TimerType.GEN5
            customTab -> TimerType.CUSTOM
            else -> throw IllegalStateException("unable to find TimerType for selected tab")
        }
    }

    private fun updateUpdateBtnDisableProperty(timerType: TimerType) {
        when (timerType) {
        }
    }
}