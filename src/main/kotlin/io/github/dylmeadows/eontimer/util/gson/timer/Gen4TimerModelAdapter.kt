package io.github.dylmeadows.eontimer.util.gson.timer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen4TimerModel
import org.springframework.stereotype.Component

@Component
class Gen4TimerModelAdapter : TypeAdapter<Gen4TimerModel>() {
    override fun write(writer: JsonWriter, value: Gen4TimerModel) {
        writer
            .beginObject()
            .name("mode")
            .value(value.mode.name)
            .name("calibratedDelay")
            .value(value.calibratedDelay)
            .name("calibratedSecond")
            .value(value.calibratedSecond)
            .name("targetDelay")
            .value(value.targetDelay)
            .name("targetSecond")
            .value(value.targetSecond)
            .endObject()
    }

    override fun read(reader: JsonReader): Gen4TimerModel {
        val model = Gen4TimerModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "mode" -> model.mode = Gen4TimerMode.valueOf(reader.nextString())
                "calibratedDelay" -> model.calibratedDelay = reader.nextLong()
                "calibratedSecond" -> model.calibratedSecond = reader.nextLong()
                "targetDelay" -> model.targetDelay = reader.nextLong()
                "targetSecond" -> model.targetSecond = reader.nextLong()
            }
        }
        reader.endObject()
        return model
    }
}