package controller;





import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.scene.control.CheckBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MenuController{
	
	
		@FXML
		private Button btnPlay, btnOption, btnQuit;
		
		@FXML
		private Label label;
		
		@FXML
		private CheckBox musicCheckBox;
		
		private Sound sound = new Sound("ressources/sounds"); 
				
		
		@FXML
		protected void onButtonClicked(ActionEvent event) {
			
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
		protected void onButtonOptionClicked(ActionEvent event) {
		      sound.playMedia(0);
		}
		
		@FXML
		protected void onChecked(ActionEvent event) {
		      sound.stopMedia();
		}
		
		
}
