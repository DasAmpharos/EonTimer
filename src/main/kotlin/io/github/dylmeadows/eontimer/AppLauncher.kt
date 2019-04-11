package io.github.dylmeadows.eontimer

import io.github.dylmeadows.eontimer.config.AppProperties
import io.github.dylmeadows.eontimer.model.resource.CssResource
import io.github.dylmeadows.eontimer.model.resource.FxmlResource
import io.github.dylmeadows.eontimer.util.*
import io.github.dylmeadows.eontimer.util.reactor.FluxFactory
import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.application.Application.launch
import javafx.scene.Parent
import javafx.stage.Stage
import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@SpringBootApplication
@ComponentScan(value = ["io.github.dylmeadows.*"])
open class AppLauncher : SpringJavaFxApplication() {

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
    // launch(AppLauncher::class.java, *args)
    var targetFrame = -1
    val sub = FluxFactory.timer(5L.milliseconds)
        .takeUntil { targetFrame >= 0 }
        .doOnNext { println(it) }
        .doOnNext {
            if (it.elapsed >= 5000) {
                targetFrame = 1
            }
        }
        .last()
        .subscribe { println("last: $it") }

    while (!sub.isDisposed) {}
}
