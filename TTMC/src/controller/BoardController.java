package controller;




import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class BoardController {
	
	
		@FXML
		private Button btnBack; 
		
		
		@FXML
		protected void onButtonClicked(ActionEvent event) {
			
			try {
				// Load the FXML file of the new interface
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menuView.fxml"));
				Pane root = fxmlLoader.load();

				// Create a new scene with the loaded content
				Scene scene = new Scene(root);

				// Get the current scene and the current stage (window)
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

				// Set the new scene on the current stage
				stage.setScene(scene);

				// Show the new scene
				stage.show();
				} catch (IOException e) {
				    e.printStackTrace();
			}
			
		}
}
