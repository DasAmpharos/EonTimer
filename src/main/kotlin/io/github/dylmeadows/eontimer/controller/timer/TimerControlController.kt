package io.github.dylmeadows.eontimer.controller.timer

import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.service.TimerService
import io.github.dylmeadows.eontimer.util.changesAsFlux
import javafx.fxml.FXML
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TimerControlController @Autowired constructor(
    private val model: ApplicationModel,
    private val timerService: TimerService) {

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

    companion object {
        private val log = LoggerFactory.getLogger(TimerControlController::class.java)
    }

    fun initialize() {
        timerTabPane.selectionModel.selectedItemProperty()
            .changesAsFlux()
            .map { it.newValue }
            .map { getTimerType(it) }
            .subscribe({
                model.selectedTimerType = it
            }, {
                log.error(it.message, it)
                System.exit(-1)
            })
    }

    private fun getTimerType(tab: Tab): TimerType {
        return when(tab) {
            gen3Tab -> TimerType.GEN3
            gen4Tab -> TimerType.GEN4
            gen5Tab -> TimerType.GEN5
            customTab -> TimerType.CUSTOM
            else -> throw IllegalStateException("unable to find TimerType for selected tab")
        }
    }
}