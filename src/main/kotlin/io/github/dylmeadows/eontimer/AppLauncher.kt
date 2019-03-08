package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.addCss
import io.github.dylmeadows.eontimer.util.asScene
import io.github.dylmeadows.eontimer.util.load
import io.github.dylmeadows.eontimer.util.log
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.application.Application.launch
import javafx.scene.Parent
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
        stage.scene = load<Parent>(FxmlResource.TimerControlPane).asScene()
        stage.scene.addCss(CssResource.MAIN)
        stage.show()
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}