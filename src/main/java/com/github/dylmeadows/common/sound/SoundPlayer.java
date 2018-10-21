package com.github.dylmeadows.common.sound;

import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import com.github.dylmeadows.eontimer.util.extension.OptionalExtensions;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Helper class for loading and playing audio clips.
 */
@Slf4j
public class SoundPlayer {

    private MediaPlayer mediaPlayer;

    private ObjectProperty<SoundResource> sound = new SimpleObjectProperty<>();

    public void play() {
        OptionalExtensions.ifPresentOrElse(sound.get(), sound -> {
            log.debug("playing sound: {}", sound.toString());
            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        }, () -> log.warn("sound not set"));
    }

    public SoundResource getSound() {
        return sound.get();
    }

    public ObjectProperty<SoundResource> soundProperty() {
        return sound;
    }

    public void setSound(SoundResource sound) {
        this.sound.set(sound);
        this.mediaPlayer = Optional.ofNullable(sound)
                .map(SoundResource::get)
                .map(Object::toString)
                .map(Media::new)
                .map(MediaPlayer::new)
                .orElse(null);
    }
}
