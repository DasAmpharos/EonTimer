package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.Dimension
import io.github.dylmeadows.eontimer.util.addCss
import io.github.dylmeadows.eontimer.util.asScene
import io.github.dylmeadows.eontimer.util.load
import io.github.dylmeadows.eontimer.util.size
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
        val properties = getBean(AppProperties::class.java)
        stage.title = "${properties.name} v${properties.version}"
        stage.scene = load<Parent>(FxmlResource.EonTimerPane).asScene()
        stage.size = Dimension(610.0, 470.0)
        stage.scene.addCss(CssResource.MAIN)
        stage.isResizable = false
        stage.show()
    }
}

fun main(args: Array<String>) {
    launch(AppLauncher::class.java, *args)
}
