package io.github.dylmeadows.eontimer.util.gson.timer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.timer.CustomTimerModel
import io.github.dylmeadows.eontimer.util.gson.TimerStageModelAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomTimerModelAdapter @Autowired constructor(
    private val timerStageModelAdapter: TimerStageModelAdapter) : TypeAdapter<CustomTimerModel>() {

    override fun write(writer: JsonWriter, value: CustomTimerModel) {
        writer.beginObject()
        writer.name("stages")
        writer.beginArray()
        value.stages.forEach { timerStageModelAdapter.write(writer, it) }
        writer.endArray()
        writer.endObject()
    }

    override fun read(reader: JsonReader): CustomTimerModel {
        val model = CustomTimerModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "stages" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        model.stages.add(timerStageModelAdapter.read(reader))
                    }
                    reader.endArray()
                }
            }
        }
        reader.endObject()
        return model
    }
}