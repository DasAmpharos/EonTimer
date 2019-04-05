package io.github.dylmeadows.eontimer.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.TimerStage
import org.springframework.stereotype.Component

@Component
class TimerStageModelAdapter : TypeAdapter<TimerStage>() {
    override fun write(writer: JsonWriter, value: TimerStage) {
        writer.value(value.length)
    }

    override fun read(reader: JsonReader): TimerStage {
        return TimerStage(reader.nextLong())
    }
}