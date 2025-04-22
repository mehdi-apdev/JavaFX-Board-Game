package application;

import java.util.List;
import java.util.Optional;

import controller.Sound;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import models.QuestionLoader;
import models.DialogWindow;
import models.Question;
import models.QuestionCard;
import models.QuestionCardFactory;

public class Main extends Application {
	
	//sound accessible to all class
	private static Sound mainSound = new Sound();
	
	@Override
	public void start(Stage primaryStage) {
		try {
				Parent root = (Parent)FXMLLoader.load(getClass().getResource("../view/menuView.fxml"));
				
				// Obtenir les dimensions de l'écran
		        double screenWidth = Screen.getPrimary().getBounds().getWidth();
		        double screenHeight = Screen.getPrimary().getBounds().getHeight();
		        
				Scene scene = new Scene(root,1536,864);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.setTitle("TTMC");
		        primaryStage.setResizable(false);

				
				//play music
				mainSound.playMedia("georgeTheme.mp3", 0.05);
				mainSound.loop();
		
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					
				@Override
				public void handle(WindowEvent event) {
					System.out.println(event.getEventType());
						
					DialogWindow alert = new DialogWindow();
					boolean result = alert.showConfirmationDialog("Quit", "Do you really want to quit the game?");
						
					if(result) {
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
	
	
	public static Sound getMainSound() {
		return mainSound;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}












