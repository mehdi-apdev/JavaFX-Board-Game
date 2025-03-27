package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.DialogWindow;

/**
 * Controller for the player choice view where players can select their color.
 * This class handles the player color selection and navigation between views.
 */
public class PlayerChoiceViewController {
    
    /**
     * Enum representing available player colors
     */
    public enum PlayerColor {
        RED(Color.RED),
        ORANGE(Color.ORANGE),
        YELLOW(Color.YELLOW),
        BLUE(Color.BLUE);
        
        private final Color color;
        
        PlayerColor(Color color) {
            this.color = color;
        }
        
        public Color getColor() {
            return color;
        }
        
        public PlayerColor next() {
            PlayerColor[] values = PlayerColor.values();
            return values[(this.ordinal() + 1) % values.length];
        }
        
        public PlayerColor previous() {
            PlayerColor[] values = PlayerColor.values();
            return values[(this.ordinal() + values.length - 1) % values.length];
        }
    }
    
    // Constants
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final String CLICK_SOUND = "click.wav";
    private static final String CONFIRM_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.5;
    
    
    // FXML elements
    @FXML private Button btnBack, btnPlay, btnOk, btnPrevious, btnNext;
    @FXML private Circle playerColor;
    @FXML private ImageView volumeImage;
    @FXML private TextField playerName;
    @FXML private Pane playerChoicePane;
    
    // Controller state
    private PlayerColor currentPlayerColor;
    private final Sound touchSound = new Sound();
    private static Paint selectedColor = Color.RED; // default color
    private static List<String> selectedListPlayersNames = new ArrayList<>() ;
    private List<String>listPlayersNames = new ArrayList<>();
    //Windows for aletts
    private DialogWindow dialog = new DialogWindow();
    
    /**
     * Initializes the controller.
     * Sets up the initial player color and sound settings.
     */
    @FXML
    public void initialize() {
        // Initialize the first color
        currentPlayerColor = PlayerColor.RED;
        playerColor.setFill(currentPlayerColor.getColor());
      

        
        // Initialize sound settings
        updateSoundDisplay();
    }
    
    /**
     * Updates the sound icon based on the current mute state.
     */
    private void updateSoundDisplay() {
        boolean isMuted = Main.mainSound.isMuted();
        volumeImage.setImage(new Image(isMuted ? VOLUME_OFF_IMAGE : VOLUME_ON_IMAGE));
    }
    
    /**
     * Handles the back button click event.
     * Returns to the main menu.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonBackClicked(ActionEvent event) {
        navigateToView("../view/menuView.fxml", event, CONFIRM_SOUND);
    }
    
    /**
     * Handles the play button click event.
     * Navigates to the game board.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonPlayClicked(ActionEvent event) {
    	touchSound.playMedia(CONFIRM_SOUND, SOUND_VOLUME);
    	if (listPlayersNames.size() < 2 ) {
    		dialog.showAlert("HEADS UP !", "You need more players to start the adventure !");
    		return;
    	}
        selectedColor = playerColor.getFill();
        selectedListPlayersNames = getListPlayersNames();
        navigateToView("../view/boardView.fxml", event, CONFIRM_SOUND);
    }
    
    /**
     * Handles navigation to a different view.
     * 
     * @param fxmlPath The path to the FXML file
     * @param event The action event that triggered the navigation
     * @param soundFile The sound file to play during navigation
     */
    private void navigateToView(String fxmlPath, ActionEvent event, String soundFile) {
        try {
            // Play sound
            touchSound.playMedia(soundFile, SOUND_VOLUME);
            
            // Load the FXML file of the new interface
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = fxmlLoader.load();
            
            // Create a new scene with the loaded content
            Scene scene = new Scene(root);
            
            // Get the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
            
            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    	if (Main.mainSound.isMuted()) {
            Main.mainSound.unMuteMedia();
        } else {
            Main.mainSound.muteMedia();
        }
        updateSoundDisplay();
    }
    
    
    /**
     * Handles the previous button click event.
     * Cycles to the previous player color.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonPreviousClicked(ActionEvent event) {
        touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        currentPlayerColor = currentPlayerColor.previous();
        playerColor.setFill(currentPlayerColor.getColor());
    }
    
    /**
     * Handles the next button click event.
     * Cycles to the next player color.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonNextClicked(ActionEvent event) {
        touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        currentPlayerColor = currentPlayerColor.next();
        playerColor.setFill(currentPlayerColor.getColor());
    }
    
    /**
     * Handles the OK button click event.
     * Confirms the current color selection.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonOkClicked(ActionEvent event) {
        touchSound.playMedia(CONFIRM_SOUND, SOUND_VOLUME);
        
        if(listPlayersNames.size() >= 4 ){
        	dialog.showAlert("OOPS!", "The game is already full ! No more players can join.");
            return;

        }
        
       if(playerName.getText().equals("")) {
    	   dialog.showAlert("HOLD UP!", "You need to enter a name to join the game!");
    	    return;
       }
       
       	selectedColor = playerColor.getFill();
        listPlayersNames.add(playerName.getText());
        System.out.println(listPlayersNames);
        dialog.showAlert("SUCESS!", "Player " + playerName.getText() + " has joined the game! Get ready to play!");
        playerName.setText("");
    }
    
    /**
     * Returns the currently selected player color.
     * 
     * @return The selected Paint color
     */
    public Paint getColor() {
        return playerColor.getFill();
    }
    
   public List<String> getListPlayersNames() {
	   return listPlayersNames;
   }
   
   public static List<String> getSelectedListPlayersNames() {
	return selectedListPlayersNames;
}
    
    /**
     * Returns the statically stored selected color.
     * This can be accessed from other controllers.
     * 
     * @return The selected Paint color
     */
    public static Paint getSelectedColor() {
        return selectedColor;
    }
}
