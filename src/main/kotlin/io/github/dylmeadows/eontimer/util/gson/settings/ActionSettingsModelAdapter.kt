package io.github.dylmeadows.eontimer.util.gson.settings

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.dylmeadows.common.javafx.scene.paint.Colors
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionMode
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import javafx.scene.paint.Color
import org.springframework.stereotype.Component

@Component
class ActionSettingsModelAdapter : TypeAdapter<ActionSettingsModel>() {

    override fun write(writer: JsonWriter, value: ActionSettingsModel) {
        writer
            .beginObject()
            .name("mode")
            .value(value.mode.name)
            .name("color")
            .value(Colors.toHex(value.color))
            .name("sound")
            .value(value.sound.name)
            .name("interval")
            .value(value.interval)
            .name("count")
            .value(value.count)
            .endObject()
    }

    override fun read(reader: JsonReader): ActionSettingsModel {
        val model = ActionSettingsModel()
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "mode" -> model.mode = ActionMode.valueOf(reader.nextString())
                "color" -> model.color = Color.web(reader.nextString())
                "sound" -> model.sound = SoundResource.valueOf(reader.nextString())
                "interval" -> model.interval = reader.nextInt()
                "count" -> model.count = reader.nextInt()
            }
        }
        reader.endObject()
        return model
    }
}