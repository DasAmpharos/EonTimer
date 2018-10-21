package com.github.dylmeadows;

import com.github.dylmeadows.common.sound.SoundPlayer;
import com.github.dylmeadows.eontimer.model.resources.SoundResource;
import com.google.common.base.Stopwatch;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaPlayerTest extends Application {

    private static final int ITERATIONS = 10;

    @Override
    public void start(Stage stage) {
        SoundPlayer soundPlayer = new SoundPlayer();
        soundPlayer.setSound(SoundResource.BEEP);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), event -> soundPlayer.play()));
        timeline.setCycleCount(6);
        timeline.play();
    }

    private void benchmarkCreate(String uri) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < ITERATIONS; i++) {
            create(uri);
        }
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void create(String uri) {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(uri));
        mediaPlayer.play();
    }

    private void benchmarkReuse(String uri) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(uri));
        for (int i = 0; i < ITERATIONS; i++) {
            reuse(mediaPlayer);
        }
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void reuse(MediaPlayer mediaPlayer) {
        mediaPlayer.seek(Duration.ZERO);
        mediaPlayer.setStartTime(Duration.ZERO);
        mediaPlayer.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
