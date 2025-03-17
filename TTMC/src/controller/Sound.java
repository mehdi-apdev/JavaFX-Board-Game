package controller;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Sound {

    private List<File> sounds;
    private MediaPlayer mediaPlayer;

    // Constructor to initialize media files
    public Sound(String directoryPath) {
        sounds = new ArrayList<>();
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                sounds.add(file);
            }
        }
    }

    // Method to play a specific media file
    public void playMedia(int index) {
        if (index >= 0 && index < sounds.size()) {
            Media media = new Media(sounds.get(index).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } else {
            System.out.println("Invalid index for media.");
        }
    }

    // Method to stop playback
    public void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(0);
        }
    }

    // Method to get the available media files
    public List<File> getSounds() {
        return sounds;
    }
}
	