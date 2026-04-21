package com.wecib.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Loops background music for the duration of the application.
 */
public final class GameMusic {

    private static final String RESOURCE = "/com/wecib/audio/theme_bgm.mp3";

    private static MediaPlayer player;

    private GameMusic() {}

    public static void start() {
        try {
            var url = GameMusic.class.getResource(RESOURCE);
            if (url == null) {
                return;
            }
            Media media = new Media(url.toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.45);
            player.play();
        } catch (RuntimeException ignored) {
            // Missing codec or invalid media — game continues without music
        }
    }

    public static void stop() {
        if (player != null) {
            player.stop();
            player.dispose();
            player = null;
        }
    }
}
