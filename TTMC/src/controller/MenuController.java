package controller;

import java.io.IOException;
import application.Main;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.DialogWindow;


/**
 * Controller for the main menu of the application.
 * Handles navigation to other views and basic application controls.
 */
public class MenuController {
    
    // Constants for resource paths
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final String CLICK_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.1;
    
    // FXML annotated UI components
    @FXML private Button btnPlay, btnOption, btnQuit;
    @FXML private Pane menuBoard;
    @FXML private Label label;
    @FXML private ImageView volumeImage;
    @FXML private Pane optionPane;
    @FXML private CheckBox timerSoundCheckBox, secondarySoundCheckBox;
    //all Sounds effects
    private static Sound touchSound = new Sound();
    private static Sound timerSound = new Sound();
    private static Sound zoomSound = new Sound();
    private static Sound secondarySound = new Sound();
    //Windows for aletts
    private DialogWindow dialog = new DialogWindow();
    
    /**
     * Initializes the controller.
     * Sets up the initial sound settings based on the application state.
     */
    @FXML
    public void initialize() {
        updateSoundDisplay();
    }
    
    /**
     * Updates the sound control UI elements based on the current mute state.
     */
    private void updateSoundDisplay() {
        boolean isMuted = Main.getMainSound().isMuted();
        volumeImage.setImage(new Image(isMuted ? VOLUME_OFF_IMAGE : VOLUME_ON_IMAGE));
    }
    
    /**
     * Handles the play button click event.
     * Navigates to the player choice view.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonClicked(ActionEvent event) {
    	touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        navigateToView("../view/playerChoiceView.fxml", event);
    }
    
    /**
     * Handles the options button click event.
     * Currently only plays a sound effect.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonOptionClicked(ActionEvent event) {
    	 touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
    	if(optionPane.isVisible()) {
			optionPane.setVisible(false);
		} else {
			optionPane.setVisible(true);
    	} 
    }


    /**
     * Handles the music image toggle event.
     * Mutes or unmutes the game sound.
     * 
     * @param event The action event
     */
    @FXML
    protected void onVolumeClicked(MouseEvent event) {
    	if (Main.getMainSound().isMuted()) {
            Main.getMainSound().unMuteMedia();
        } else {
            Main.getMainSound().muteMedia();
        }
        updateSoundDisplay();
    }
    
    /**
     * Handles the quit button click event.
     * Displays a confirmation dialog before exiting the application.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonQuitClicked(ActionEvent event) {
        zoomSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        boolean result;
        result = dialog.showConfirmationDialog("QUIT GAME", "Do you really want to leave the adventure?");
        if (result) {
        	Platform.exit();
        }
    }
    
    /**
     * Navigates to a different view.
     * 
     * @param fxmlPath The path to the FXML file to load
     * @param event The action event that triggered the navigation
     */

	private void navigateToView(String fxmlPath, ActionEvent event) {
		try {
			touchSound.playMedia("slide.wav", 0.5);

			// Get the current scene
			Node source = (Node) event.getSource();
			//Stage stage = (Stage) source.getScene().getWindow();
			Scene currentScene = source.getScene();
			Pane currentRoot = (Pane) currentScene.getRoot();

			// Preload the new interface
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
			Pane newRoot = fxmlLoader.load();

			// Use a StackPane to overlay the views
			StackPane stackPane = new StackPane(currentRoot, newRoot);
			
			currentScene.setRoot(stackPane);

			// Position the new view off-screen (to the right)
			newRoot.setTranslateX(currentScene.getWidth());

			// Create slide-out transition for the current view
			TranslateTransition slideOut = new TranslateTransition(Duration.millis(800), currentRoot);
			slideOut.setToX(-currentScene.getWidth());

			// Create slide-in transition for the new view
			TranslateTransition slideIn = new TranslateTransition(Duration.millis(800), newRoot);
			slideIn.setToX(0);

			// After the animation, remove the old root and set the new root
			slideOut.setOnFinished(e -> {
				stackPane.getChildren().remove(currentRoot); // Remove the old root
			
			});

			// Play the animations
			slideOut.play();
			slideIn.play();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // Getters for sound effects
    public static Sound getTimerSound() {
		return timerSound;
	}
    // Getters for sound effects
    public static Sound getTouchSound() {
		return touchSound;
	}
    // Getters for sound effects
    public static Sound getSecondarySound() {
		return secondarySound;
	}
    
}
