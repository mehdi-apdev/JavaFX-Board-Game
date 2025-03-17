package controller;




import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

public class MenuController implements Initializable{
	
	
		@FXML
		private Button btnPlay;
		
		@FXML
		private Button btnOption; 
		
		@FXML
		private Label label;
		
		private MediaPlayer mediaPlayer;
		private Media media;
		private File directory;
		private File[] files;
		private ArrayList<File>sounds; 
		
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

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			sounds = new ArrayList<File>();
			directory = new File("ressources/sounds");
			files = directory.listFiles();
			if(files != null) {
				for (File file : files) {
					sounds.add(file);
					System.out.println(file);
				}
			}
			
			
			media = new Media(sounds.get(0).toURI().toString()); 
			mediaPlayer = new MediaPlayer(media);
			
			
		}
		
		
		@FXML
		protected void onButtonOptionClicked(ActionEvent event) {
		      mediaPlayer.play();  
		}
		
		
}
