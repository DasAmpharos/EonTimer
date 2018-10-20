package com.github.dylmeadows.common.sound;

import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Helper class for loading and playing audio clips.
 */
public class SoundPlayer {

    private SoundResource sound;
    private MediaPlayer mediaPlayer;

    private static final Logger logger = LoggerFactory.getLogger(SoundPlayer.class);

    public SoundPlayer(SoundResource sound) {
        this.sound = sound;
        this.mediaPlayer = new MediaPlayer(new Media(sound.getPath()));
    }

    public void play() {
        if (Objects.nonNull(sound)) {
            logger.debug("playing sound: {}", sound.toString());
            mediaPlayer.setStartTime(Duration.ZERO);
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        } else {
            logger.warn("sound not set");
        }
    }
}
