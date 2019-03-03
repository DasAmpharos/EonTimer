package io.github.dylmeadows.eontimer.util.gson.timer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen3TimerModel
import org.springframework.stereotype.Component

@Component
class Gen3TimerModelAdapter : TypeAdapter<Gen3TimerModel>() {
    override fun write(writer: JsonWriter, value: Gen3TimerModel) {
        writer
            .beginObject()
            .name("mode")
            .value(value.mode.name)
            .name("calibration")
            .value(value.calibration)
            .name("preTimer")
            .value(value.preTimer)
            .name("targetFrame")
            .value(value.targetFrame)
            .endObject()
    }

    override fun read(reader: JsonReader): Gen3TimerModel {
        val model = Gen3TimerModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "mode" -> model.mode = Gen3TimerMode.valueOf(reader.nextString())
                "calibration" -> model.calibration = reader.nextInt()
                "preTimer" -> model.preTimer = reader.nextInt()
                "targetFrame" -> model.targetFrame = reader.nextInt()
            }
        }
        reader.endObject()
        return model
    }
}