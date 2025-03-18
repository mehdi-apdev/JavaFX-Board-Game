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
    private boolean isMuted; 

    // Constructor to initialize media files
    public Sound() {
    	
    	isMuted = false;
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
    public void playMedia(String nameFile, double volume) {
        boolean fileFound = false;
        for (File sound : sounds) {
            if (sound.getName().toString().equals(nameFile)) {
                Media media = new Media(sound.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                mediaPlayer.setVolume(volume);
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
            mediaPlayer.stop();
        }
    }
    
    //Method to loop 
    public void loop() {
        if (mediaPlayer != null) {
            mediaPlayer.setOnEndOfMedia(() -> {
                mediaPlayer.seek(javafx.util.Duration.ZERO); //
                mediaPlayer.play(); // 
            });
        }
    }
    
    // Method to mute playback
    public void muteMedia() {
        if (mediaPlayer != null) {
        	isMuted = true;
            mediaPlayer.setVolume(0);
        }
    }
    
 // Method to unmute playback
    public void unMuteMedia() {
        if (mediaPlayer != null) {
        	isMuted = false;
            mediaPlayer.setVolume(0.3);
        }
    }


    // Method to get the available media files
    public List<File> getSounds() {
        return sounds;
    }
    
    public boolean isMuted() {
		return isMuted;
	}
    
}
	