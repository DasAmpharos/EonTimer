package io.github.dylmeadows.eontimer.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.ApplicationModel
import io.github.dylmeadows.eontimer.model.timer.TimerType
import io.github.dylmeadows.eontimer.util.gson.settings.ActionSettingsModelAdapter
import io.github.dylmeadows.eontimer.util.gson.settings.TimerSettingsModelAdapter
import io.github.dylmeadows.eontimer.util.gson.timer.CustomTimerModelAdapter
import io.github.dylmeadows.eontimer.util.gson.timer.Gen3TimerModelAdapter
import io.github.dylmeadows.eontimer.util.gson.timer.Gen4TimerModelAdapter
import io.github.dylmeadows.eontimer.util.gson.timer.Gen5TimerModelAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ApplicationModelAdapter @Autowired constructor(
    private val gen3TimerModelAdapter: Gen3TimerModelAdapter,
    private val gen4TimerModelAdapter: Gen4TimerModelAdapter,
    private val gen5TimerModelAdapter: Gen5TimerModelAdapter,
    private val customTimerModelAdapter: CustomTimerModelAdapter,
    private val actionSettingsModelAdapter: ActionSettingsModelAdapter,
    private val timerSettingsModelAdapter: TimerSettingsModelAdapter) : TypeAdapter<ApplicationModel>() {

    override fun write(writer: JsonWriter, value: ApplicationModel) {
        writer.beginObject()
        gen3TimerModelAdapter.write(writer.name("gen3"), value.gen3)
        gen4TimerModelAdapter.write(writer.name("gen4"), value.gen4)
        gen5TimerModelAdapter.write(writer.name("gen5"), value.gen5)
        customTimerModelAdapter.write(writer.name("custom"), value.custom)
        actionSettingsModelAdapter.write(writer.name("actionSettings"), value.actionSettings)
        timerSettingsModelAdapter.write(writer.name("timerSettings"), value.timerSettings)
        writer.name("selectedTimer").value(value.selectedTimerType.name)
        writer.endObject()
    }

    override fun read(reader: JsonReader): ApplicationModel {
        val model = ApplicationModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "gen3" -> model.gen3 = gen3TimerModelAdapter.read(reader)
                "gen4" -> model.gen4 = gen4TimerModelAdapter.read(reader)
                "gen5" -> model.gen5 = gen5TimerModelAdapter.read(reader)
                "custom" -> model.custom = customTimerModelAdapter.read(reader)
                "actionSettings" -> model.actionSettings = actionSettingsModelAdapter.read(reader)
                "timerSettings" -> model.timerSettings = timerSettingsModelAdapter.read(reader)
                "selectedTimer" -> model.selectedTimerType = TimerType.valueOf(reader.nextString())
            }
        }
        reader.endObject()
        return model
    }
}