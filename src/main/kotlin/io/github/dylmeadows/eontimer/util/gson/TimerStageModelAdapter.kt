package io.github.dylmeadows.eontimer.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.Stage
import org.springframework.stereotype.Component

@Component
class TimerStageModelAdapter : TypeAdapter<Stage>() {
    override fun write(writer: JsonWriter, value: Stage) {
        writer.value(value.length)
    }

    override fun read(reader: JsonReader): Stage {
        return Stage(reader.nextLong())
    }
}