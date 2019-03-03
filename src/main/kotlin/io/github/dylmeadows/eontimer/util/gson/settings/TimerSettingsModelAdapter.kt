package io.github.dylmeadows.eontimer.util.gson.settings

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.settings.Console
import io.github.dylmeadows.eontimer.model.settings.TimerSettingsModel
import org.springframework.stereotype.Component

@Component
class TimerSettingsModelAdapter : TypeAdapter<TimerSettingsModel>() {
    override fun write(writer: JsonWriter, value: TimerSettingsModel) {
        writer
            .beginObject()
            .name("console")
            .value(value.console.name)
            .name("refreshInterval")
            .value(value.refreshInterval)
            .name("precisionCalibrationMode")
            .value(value.precisionCalibrationMode)
            .name("minimumLength")
            .value(value.minimumLength)
            .endObject()
    }

    override fun read(reader: JsonReader): TimerSettingsModel {
        val model = TimerSettingsModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "console" -> model.console = Console.valueOf(reader.nextString())
                "refreshInterval" -> model.refreshInterval = reader.nextInt()
                "precisionCalibrationMode" -> model.precisionCalibrationMode = reader.nextBoolean()
                "minimumLength" -> model.minimumLength = reader.nextInt()
            }
        }
        reader.endObject()
        return model
    }
}