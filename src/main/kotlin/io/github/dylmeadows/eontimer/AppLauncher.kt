package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.controller.settings.SettingsDialogController
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.addCss
import io.github.dylmeadows.eontimer.util.asScene
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.application.Application.launch
import javafx.scene.Parent
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(value = ["io.github.dylmeadows.*"])
open class AppLauncher : SpringJavaFxApplication() {

    private val log = LoggerFactory.getLogger(AppLauncher::class.java)

    override fun onInit() {
        arrayOf("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .forEach { log.info("{} == {}", it, System.getProperty(it)) }
    }

    override fun start(stage: Stage) {

        val controller = getBean(SettingsDialogController::class.java)
        controller.showAndWait()
        /*val parent = load<Parent>(FxmlResource.TimerControlPane.get())
        stage.scene = parent.asScene()
        stage.show()*/
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}