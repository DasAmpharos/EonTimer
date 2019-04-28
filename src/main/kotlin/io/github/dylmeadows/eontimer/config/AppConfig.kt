package io.github.dylmeadows.eontimer.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import io.github.dylmeadows.eontimer.util.gson.ApplicationModelAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import javax.annotation.PreDestroy

@Configuration
@EnableConfigurationProperties(AppProperties::class)
open class AppConfig @Autowired constructor(
    private val properties: AppProperties,
    private val context: ApplicationContext) {

    @PreDestroy
    private fun destroy() {
        val gson = context.getBean(Gson::class.java)
        val settings = context.getBean(ApplicationModel::class.java)
        // persist settings
        val json = gson.toJson(settings)
        File("${properties.name}.json")
            .writeText(json)
    }

    @Bean
    open fun gson(builder: GsonBuilder,
                  applicationModelAdapter: ApplicationModelAdapter): Gson {
        return builder.setPrettyPrinting()
            .registerTypeAdapter(ApplicationModel::class.java, applicationModelAdapter)
            .create()
    }

    @Bean
    open fun settings(gson: Gson): ApplicationModel {
        val file = File("${properties.name}.json")
        return if (file.exists()) {
            gson.fromJson(file.readText(), ApplicationModel::class.java)
        } else {
            ApplicationModel()
        }
    }

    @Bean
    open fun gen3TimerModel(settings: ApplicationModel): Gen3TimerModel = settings.gen3

    @Bean
    open fun gen4TimerModel(settings: ApplicationModel): Gen4TimerModel = settings.gen4

    @Bean
    open fun gen5TimerModel(settings: ApplicationModel): Gen5TimerModel = settings.gen5

    @Bean
    open fun customTimerModel(settings: ApplicationModel): CustomTimerModel = settings.custom

    @Bean
    open fun actionSettingsModel(settings: ApplicationModel): ActionSettingsModel = settings.actionSettings

    @Bean
    open fun timerSettingsModel(settings: ApplicationModel): TimerSettingsModel = settings.timerSettings
}