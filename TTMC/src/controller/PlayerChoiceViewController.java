package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import models.Player;
import view.PlayerView;

public class PlayerChoiceViewController {
	

	@FXML
	private Button btnBack, btnPlay; 
	private Game game; //game object
	private PlayerView playerView; //player view object
	@FXML
	private CheckBox musicCheckBox;
	@FXML
	private ImageView volumeImage;
	
	
	 @FXML
	    public void initialize() {
	     
		 //do a shared class to avoid repetition
		 if (Main.mainSound.isMuted()== true) {
	        	musicCheckBox.setSelected(false);;
	        	Image noVolume = new Image("file:ressources/images/noVolume.png"); 
				volumeImage.setImage(noVolume);
			}else {
				musicCheckBox.setSelected(true);
				Image maxVolume = new Image("file:ressources/images/maxVolume.png"); 
				volumeImage.setImage(maxVolume);
			}

	    }

	
	
	@FXML
    protected void onButtonBackClicked(ActionEvent event) {
        try {
        	
            // Load the FXML file of the new interface
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/menuView.fxml"));
            Pane root = fxmlLoader.load();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Get the current scene and the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());

            // Set the new scene on the current stage
            stage.setScene(scene);

            // Show the new scene
            stage.show();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@FXML
    protected void onButtonPlayClicked(ActionEvent event) {
        try {
        	
            // Load the FXML file of the new interface
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/boardView.fxml"));
            Pane root = fxmlLoader.load();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Get the current scene and the current stage (window)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());

            // Set the new scene on the current stage
            stage.setScene(scene);

            // Show the new scene
            stage.show();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	@FXML
	protected void onChecked(ActionEvent event) {
		
		// Check the current mute status and toggle it
		if (Main.mainSound.isMuted()) { 
			Image maxVolume = new Image("file:ressources/images/maxVolume.png"); 
			volumeImage.setImage(maxVolume);
			Main.mainSound.unMuteMedia(); //method to unmute the media
		} else {
			Main.mainSound.muteMedia(); //method to mute the media
			Image noVolume = new Image("file:ressources/images/noVolume.png"); 
			volumeImage.setImage(noVolume);
		}
	}
	

}
