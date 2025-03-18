package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.scene.control.CheckBox;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MenuController{
	
		@FXML
		private Button btnPlay, btnOption, btnQuit;
		@FXML
		private Pane menuBoard;
		@FXML
		private Label label;
		@FXML
		private CheckBox musicCheckBox;
		@FXML
		private ImageView volumeImage;
		
		private Sound touchSound = new Sound(); 
		
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
		protected void onButtonClicked(ActionEvent event) {
			
			try {
				

				//Play sound
				touchSound.playMedia("click2.wav", 0.5);
				
				// Load the FXML file of the new interface
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/playerChoiceView.fxml"));
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
		protected void onButtonOptionClicked(ActionEvent event) {
			//Play sound
			touchSound.playMedia("click2.wav", 0.5);
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
		protected void onButtonQuitClicked(ActionEvent event) {
			//Play sound
			touchSound.playMedia("click2.wav", 0.5);
			
			//Send Alert if the player wants to quit
			Alert alert = new Alert(AlertType.CONFIRMATION); 
			alert.setTitle("Exit Program"); 
			alert.setHeaderText("Confirm Exit");
			alert.setContentText("Are you sure that you want to exit the program?");
				
			Optional<ButtonType> result = alert.showAndWait();
			if(result.isPresent() && result.get() == ButtonType.OK) {
				
				//End program
				Platform.exit(); 
			}
			else {
				event.consume();
			}
		}
		
		
		
}
