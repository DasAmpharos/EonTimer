package io.github.dylmeadows.eontimer.service.action

import io.github.dylmeadows.eontimer.model.resource.SoundResource
import io.github.dylmeadows.eontimer.model.settings.ActionSettingsModel
import io.github.dylmeadows.eontimer.util.asFlux
import io.github.dylmeadows.eontimer.util.getValue
import io.github.dylmeadows.eontimer.util.setValue
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class SoundPlayer @Autowired constructor(
    private val actionSettings: ActionSettingsModel) {

    private val mediaPlayerProperty = SimpleObjectProperty<MediaPlayer>()
    private var mediaPlayer by mediaPlayerProperty

    @PostConstruct
    private fun initialize() {
        actionSettings.soundProperty.asFlux()
            .map { createMediaPlayer(it) }
            .doOnNext { mediaPlayer = it }
            .subscribe()
    }

    private fun createMediaPlayer(sound: SoundResource): MediaPlayer {
        return MediaPlayer(Media(sound.get().toExternalForm()))
    }

    fun play() {
        mediaPlayer.startTime = Duration.ZERO
        mediaPlayer.seek(Duration.ZERO)
        mediaPlayer.play()
    }

    fun stop() {
        mediaPlayer.stop()
    }
}