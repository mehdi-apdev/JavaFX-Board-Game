package controller;

import java.io.IOException;
import java.util.Optional;

import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;

/**
 * Controller for the main menu of the application.
 * Handles navigation to other views and basic application controls.
 */
public class MenuController {
    
    // Constants for resource paths
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final String CLICK_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.5;
    
    // FXML annotated UI components
    @FXML private Button btnPlay, btnOption, btnQuit;
    @FXML private Pane menuBoard;
    @FXML private Label label;
    @FXML private CheckBox musicCheckBox;
    @FXML private ImageView volumeImage;
    
    // Sound effect for button interactions
    private final Sound touchSound = new Sound();
    
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
        boolean isMuted = Main.mainSound.isMuted();
        musicCheckBox.setSelected(!isMuted);
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
    }
    
    /**
     * Handles the music checkbox toggle event.
     * Mutes or unmutes the game sound.
     * 
     * @param event The action event
     */
    @FXML
    protected void onChecked(ActionEvent event) {
        if (Main.mainSound.isMuted()) {
            Main.mainSound.unMuteMedia();
        } else {
            Main.mainSound.muteMedia();
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
        touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        boolean result;
        result = showConfirmationDialog("QUIT GAME", "Do you really want to leave the adventure?");
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
            touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = fxmlLoader.load();
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
 // Method to show confirmation alerts
    private boolean showConfirmationDialog(String title, String text) {
        // Créer un alert de type CONFIRMATION
        Alert alert = new Alert(AlertType.CONFIRMATION);
        
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.getDialogPane().setStyle("");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonCancel);

        // Afficher l'alerte et récupérer la réponse
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }

}
