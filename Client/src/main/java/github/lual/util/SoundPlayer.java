package github.lual.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundPlayer {

    private final MediaPlayer mediaPlayer;

    public SoundPlayer(String resource, double volume) {
        URL resourceURL = ResourceLoader.getInstance().getResourceURL(resource);
        if (resourceURL == null) {
            throw new IllegalArgumentException(String.format("Sound file %s not found", resource));
        }
        try {
            Media media = new Media(resourceURL.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(false);
            mediaPlayer.setVolume(volume);
            mediaPlayer.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to build MediaPlayer", e);
        }
    }

    public void playOnce() {
        mediaPlayer.play();
    }
}
