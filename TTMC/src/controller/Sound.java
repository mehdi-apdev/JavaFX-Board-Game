package controller;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Sound {

    private List<File> sounds;
    private MediaPlayer mediaPlayer;

    // Constructor to initialize media files
    public Sound() {
        sounds = new ArrayList<>();
        File directory = new File("ressources/sounds");
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                sounds.add(file);
            }
        }
    }

 // Method to play a specific media file
    public void playMedia(String nameFile) {
        boolean fileFound = false;
        for (File sound : sounds) {
            if (sound.getName().toString().equals(nameFile)) {
                Media media = new Media(sound.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                fileFound = true;
                break;
            }
        }
        if (!fileFound) {
            System.out.println("File not found: " + nameFile);
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
	