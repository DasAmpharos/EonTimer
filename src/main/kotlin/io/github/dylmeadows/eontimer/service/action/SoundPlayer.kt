package io.github.dylmeadows.eontimer.service.action

import io.github.dylmeadows.eontimer.model.resource.BASE_RESOURCE_PATH
import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.asFlux
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.URL
import javax.annotation.PostConstruct

@Component
class SoundPlayer @Autowired constructor(
    private val actionSettings: ActionSettingsModel) {

    private lateinit var mediaPlayer: MediaPlayer

    @PostConstruct
    private fun initialize() {
        actionSettings.soundProperty.asFlux()
            .map(SoundResource::get)
            .map(::createMediaPlayer)
            .subscribe { mediaPlayer = it }
        // NOTE: this buffers the MediaPlayer.
        // Without this buffering, the first time
        // audio is played, it is delayed.
        GlobalScope.launch(Dispatchers.JavaFx) {
            val path = "$BASE_RESOURCE_PATH/sounds/silence.wav"
            val resource = javaClass.classLoader.getResource(path)
            createMediaPlayer(resource).play()
        }
    }

    private fun createMediaPlayer(url: URL): MediaPlayer =
        MediaPlayer(Media(url.toExternalForm()))

    fun play() {
        mediaPlayer.startTime = Duration.ZERO
        mediaPlayer.seek(Duration.ZERO)
        mediaPlayer.play()
    }

    fun stop() {
        mediaPlayer.stop()
    }
}