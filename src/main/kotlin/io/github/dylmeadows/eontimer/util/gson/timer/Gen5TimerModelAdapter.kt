package io.github.dylmeadows.eontimer.util.gson.timer

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerMode
import io.github.dylmeadows.eontimer.model.timer.Gen5TimerModel
import org.springframework.stereotype.Component

@Component
class Gen5TimerModelAdapter : TypeAdapter<Gen5TimerModel>() {
    override fun write(writer: JsonWriter, value: Gen5TimerModel) {
        writer
            .beginObject()
            .name("mode")
            .value(value.mode.name)
            .name("calibration")
            .value(value.calibration)
            .name("targetDelay")
            .value(value.targetDelay)
            .name("targetSecond")
            .value(value.targetSecond)
            .name("entralinkCalibration")
            .value(value.entralinkCalibration)
            .name("frameCalibration")
            .value(value.frameCalibration)
            .name("targetAdvances")
            .value(value.targetAdvances)
            .endObject()
    }

    override fun read(reader: JsonReader): Gen5TimerModel {
        val model = Gen5TimerModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "mode" -> model.mode = Gen5TimerMode.valueOf(reader.nextString())
                "calibration" -> model.calibration = reader.nextLong()
                "targetDelay" -> model.targetDelay = reader.nextLong()
                "targetSecond" -> model.targetSecond = reader.nextLong()
                "entralinkCalibration" -> model.entralinkCalibration = reader.nextLong()
                "frameCalibration" -> model.frameCalibration = reader.nextLong()
                "targetAdvances" -> model.targetAdvances = reader.nextLong()
            }
        }
        reader.endObject()
        return model
    }
}