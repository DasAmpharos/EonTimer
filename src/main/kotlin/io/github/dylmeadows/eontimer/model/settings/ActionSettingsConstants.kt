@file:JvmName("ActionSettingsConstants")

package io.github.dylmeadows.eontimer.model.settings

import io.github.dylmeadows.eontimer.model.resource.SoundResource
import javafx.scene.paint.Color

@JvmField
val DEFAULT_MODE = ActionMode.AUDIO
@JvmField
val DEFAULT_SOUND = SoundResource.BEEP
@JvmField
val DEFAULT_COLOR = Color.CYAN!!

const val DEFAULT_INTERVAL = 500
const val DEFAULT_COUNT = 6
