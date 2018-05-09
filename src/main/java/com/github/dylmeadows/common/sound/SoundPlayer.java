package com.github.dylmeadows.common.sound;

import com.github.dylmeadows.eontimer.EonTimerLogger;
import com.github.dylmeadows.eontimer.reference.resources.SoundResource;
import com.github.dylmeadows.common.javafx.node.FxOptionPane;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class for loading and playing audio clips.
 */
public class SoundPlayer {

    /**
     * Underlying clip used for sound loading and playback.
     */
    private Clip clip;

    /**
     * Instantiates {@link #clip}.
     */
    public SoundPlayer() {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            EonTimerLogger.getLogger().error(e);
            FxOptionPane.showExceptionDialog(e);
        }
    }

    /**
     * Synchronously loads the given {@link InputStream} for playback.
     *
     * @param stream the stream to load
     * @throws LineUnavailableException      see {@link Clip#open()}
     * @throws IOException                   see {@link Clip#open()} & {@link AudioSystem#getAudioInputStream(InputStream)}
     * @throws UnsupportedAudioFileException see {@link AudioSystem#getAudioInputStream(InputStream)}
     */
    public void load(InputStream stream) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if (clip.isOpen()) clip.close();
        clip.open(AudioSystem.getAudioInputStream(stream));
    }

    /**
     * Synchronously loads the given {@link SoundResource} for playback.
     *
     * @param sound the sound resource to load
     * @throws LineUnavailableException      see {@link Clip#open()}
     * @throws IOException                   see {@link Clip#open()} & {@link AudioSystem#getAudioInputStream(InputStream)}
     * @throws UnsupportedAudioFileException see {@link AudioSystem#getAudioInputStream(InputStream)}
     */
    public void load(SoundResource sound) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        load(sound.getStream());
    }

    /**
     * Synchronously plays the loaded audio.
     */
    public void play() {
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Determines if audio has been loaded into the {@link Clip} or not.
     *
     * @return {@link Clip#isOpen()}
     */
    public boolean isLoaded() {
        return clip.isOpen();
    }
}
