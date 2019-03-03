package io.github.dylmeadows.eontimer

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

@SpringBootApplication
open class AppLauncher : SpringJavaFxApplication() {
    companion object {
        private val log = LoggerFactory.getLogger(AppLauncher::class.java)
    }

    override fun onInit() {
        arrayOf("os.name", "os.version", "os.arch", "java.version", "java.vendor", "sun.arch.data.model")
            .forEach { log.info("{} == {}", it, System.getProperty(it)) }
    }

    override fun start(stage: Stage) {
        stage.scene = load<Parent>(FxmlResource.Gen3TimerPane.asStream).asScene()
        stage.scene.addCss(CssResource.MAIN)
        stage.show()
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}