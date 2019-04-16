package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.service.CalibrationService
import io.github.dylmeadows.eontimer.service.factory.timer.VariableFrameTimer
import io.github.dylmeadows.eontimer.util.Dimension
import io.github.dylmeadows.eontimer.util.addCss
import io.github.dylmeadows.eontimer.util.asScene
import io.github.dylmeadows.eontimer.util.isIndefinite
import io.github.dylmeadows.eontimer.util.load
import io.github.dylmeadows.eontimer.util.seconds
import io.github.dylmeadows.eontimer.util.size
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.application.Application.launch
import javafx.scene.Parent
import javafx.stage.Stage
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import java.util.concurrent.CountDownLatch

@SpringBootApplication
@ComponentScan(value = ["io.github.dylmeadows.*"])
open class AppLauncher : SpringJavaFxApplication() {

    private val log = LoggerFactory.getLogger(AppLauncher::class.java)

    override fun onInit() {
        arrayOf("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .forEach { log.info("{} == {}", it, System.getProperty(it)) }
    }

    override fun start(stage: Stage) {
        val properties = getBean(AppProperties::class.java)
        stage.title = "${properties.name} v${properties.version}"
        stage.scene = load<Parent>(FxmlResource.EonTimerPane).asScene()
        stage.size = Dimension(610.0, 470.0)
        stage.scene.addCss(CssResource.MAIN)
        stage.isResizable = false
        stage.show()
    }
}

suspend fun main(args: Array<String>) {
    // launch(AppLauncher::class.java, *args)
//    val settings = TimerSettingsModel()
//    val calibrationService = CalibrationService(settings)
//    val timer = VariableFrameTimer(settings, calibrationService)
//
//    val latch = CountDownLatch(1)
//    timer.start(3000L, 0L)
//        .doOnComplete(latch::countDown)
//        .doOnNext { println("{ delta: ${it.delta.toMillis()}; elapsed: ${it.elapsed.toMillis()}; duration: ${it.duration.isIndefinite} }") }
//        .subscribe()
//
//    delay(10L.seconds.toMillis())
//    timer.targetFrame = calibrationService.toDelays(20L.seconds.toMillis())
//
//    latch.await()
}
