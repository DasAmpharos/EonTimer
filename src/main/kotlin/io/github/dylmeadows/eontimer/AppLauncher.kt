package io.github.dylmeadows.eontimer

import io.github.dylmeadows.springboot.javafx.SpringJavaFxApplication
import javafx.stage.Stage
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(AppLauncher::class.java, args)
}