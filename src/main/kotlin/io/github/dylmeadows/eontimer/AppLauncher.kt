package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FontResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.*
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.application.Application.launch
import javafx.scene.Parent
import javafx.scene.text.Font
import javafx.stage.Stage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(value = ["io.github.dylmeadows.*"])
open class AppLauncher : SpringJavaFxApplication() {

    override fun onInit() {
        arrayOf("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .forEach { log.info("{} == {}", it, System.getProperty(it)) }
    }

    override fun start(stage: Stage) {
        FontResource.ROBOTO_REGULAR.load()
        stage.scene = load<Parent>(FxmlResource.EonTimerPane).asScene()
        stage.size = Dimension(610.0, 485.0)
        stage.scene.addCss(CssResource.MAIN)
        stage.isResizable = false
        stage.show()
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}