package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
				Pane root = (Pane)FXMLLoader.load(getClass().getResource("menuView.fxml"));
				
				// Obtenir les dimensions de l'écran
		        double screenWidth = Screen.getPrimary().getBounds().getWidth();
		        double screenHeight = Screen.getPrimary().getBounds().getHeight();
		        
				Scene scene = new Scene(root,screenWidth,screenHeight);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				
		
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	
				@Override
				public void handle(WindowEvent event) {
					System.out.println(event.getEventType());
						
					Alert alert = new Alert(AlertType.CONFIRMATION); 
					alert.setTitle("Exit Program"); 
					alert.setHeaderText("Confirm Exit");
					alert.setContentText("Are you sure that you want to exit the program?");
						
					Optional<ButtonType> result = alert.showAndWait();
					if(result.isPresent() && result.get() == ButtonType.OK) {
						Platform.exit(); 
					}
					else {
						event.consume();
					}
				}
			});
				
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
