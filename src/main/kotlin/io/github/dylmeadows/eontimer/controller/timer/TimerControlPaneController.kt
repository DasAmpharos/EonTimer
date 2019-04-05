package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.TimerState
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.service.factory.TimerFactoryService
import io.github.dylmeadows.eontimer.util.asFlux
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerControlPaneController @Autowired constructor(
    private val model: ApplicationModel,
    private val timerState: TimerState,
    private val timerService: TimerService,
    private val timerFactoryService: TimerFactoryService,
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
        timerTabPane.selectionModel.select(getTimerTab())
        timerTabPane.selectionModel.selectedItemProperty().asFlux()
            .map { getTimerType(it) }
            .subscribe {
                model.selectedTimerType = it
            }

        timerState.runningProperty.asFlux()
            .map { if (!it) "Start" else "Stop" }
            .subscribe { timerBtn.text = it }
        timerBtn.setOnAction {
            if (!timerState.running) {
                timerService.start()
            } else {
                timerService.stop()
            }
        }

        updateBtn.setOnAction {
            timerFactoryService.calibrate()
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

    private fun getTimerTab(): Tab {
        return when (model.selectedTimerType) {
            TimerType.GEN3 -> gen3Tab
            TimerType.GEN4 -> gen4Tab
            TimerType.GEN5 -> gen5Tab
            TimerType.CUSTOM -> customTab
        }
    }
}