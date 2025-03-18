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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import models.Player;
import view.PlayerView;

public class PlayerChoiceViewController {
	
	public enum colorEnum{
		RED, ORANGE, YELLOW, BLUE
	}
	

	@FXML
	private Button btnBack, btnPlay, btnOk, btnPrevious, btnNext; 
	private Game game; //game object
	private PlayerView playerView; //player view object
	@FXML
	private CheckBox musicCheckBox;
	@FXML
	private Circle playerColor;
	private colorEnum playerColorEnum; 
	@FXML
	private ImageView volumeImage;
	private Sound touchSound = new Sound(); 
	
	
	 @FXML
	    public void initialize() {
		 
		 //initialize the First color 
		 playerColorEnum = colorEnum.RED;
	     
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
        	
        	//Play sound
			touchSound.playMedia("click2.wav", 0.5);
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
        	
        	//Play sound
			touchSound.playMedia("click2.wav", 0.5);
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
	
	@FXML
	protected void onButtonPreviousClicked(ActionEvent event) {
		//Play sound
		touchSound.playMedia("click.wav", 0.5);
		
		//Switch to change color
		switch (playerColorEnum) {
	    case RED:
	        playerColorEnum = colorEnum.YELLOW;
	        playerColor.setFill(Color.YELLOW);
	        break;
	    case YELLOW:
	        playerColorEnum = colorEnum.ORANGE;
	        playerColor.setFill(Color.ORANGE);
	        break;
	    case ORANGE:
	        playerColorEnum = colorEnum.BLUE;
	        playerColor.setFill(Color.BLUE);
	        break;
	    case BLUE:
	        playerColorEnum = colorEnum.RED;
	        playerColor.setFill(Color.RED);
	        break;
	}
	}
	
	@FXML
	protected void onButtonNextClicked(ActionEvent event) {
		//Play sound
		touchSound.playMedia("click.wav", 0.5);
		
		//Switch to change color
		switch (playerColorEnum) {
        case colorEnum.RED:
        	playerColorEnum = colorEnum.BLUE;
        	playerColor.setFill(Color.BLUE);
        	break;
        case colorEnum.BLUE:
        	playerColorEnum = colorEnum.ORANGE;
        	playerColor.setFill(Color.ORANGE);
            break;
        case colorEnum.ORANGE:
        	playerColorEnum = colorEnum.YELLOW;
        	playerColor.setFill(Color.YELLOW);
            break;
        case colorEnum.YELLOW:
        	playerColorEnum = colorEnum.RED;
        	playerColor.setFill(Color.RED);
           break;
		}
		
	}
		
	
	
	@FXML
	protected void onButtonOkClicked(ActionEvent event) {
		//Play sound
		touchSound.playMedia("click2.wav", 0.5);
		
	}
	
	

}
