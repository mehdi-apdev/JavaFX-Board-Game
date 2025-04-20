package models;

import javafx.util.Duration;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class DialogWindow {
	private Alert alertInfo;
	private Alert alertConfirm;
	
	public DialogWindow() {
		  alertInfo = new Alert(AlertType.INFORMATION);
		  alertConfirm = new Alert(AlertType.CONFIRMATION);
	}
	
	
	public void showAlert(String title, String message) {
		alertInfo.setHeaderText(title);
		alertInfo.setContentText(message);
		alertInfo.getDialogPane().setStyle("");
		alertInfo.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
		alertInfo.show();
		//close the alert after 1.5 seconds
	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> alertInfo.close()));
	    timeline.setCycleCount(1);
	    timeline.play();
    }
	
	
	public boolean showConfirmationDialog(String title, String text) {
		alertConfirm.setHeaderText(title);
		alertConfirm.setContentText(text);
		alertConfirm.getDialogPane().setStyle("");
		alertConfirm.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
        ButtonType buttonYes = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertConfirm.getButtonTypes().setAll(buttonYes, buttonCancel);

        // Afficher l'alerte et récupérer la réponse
        Optional<ButtonType> result = alertConfirm.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }


	public Optional<String> showInputDialog(String title, String message) {
		 TextInputDialog inputDialog = new TextInputDialog();
		 	inputDialog.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
	        inputDialog.setTitle(message);
	        inputDialog.setHeaderText(message);
	        // Créer des boutons personnalisés
	        ButtonType buttonYes = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
	        ButtonType buttonNo = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

	        // Remplacer les boutons par défaut
	        inputDialog.getDialogPane().getButtonTypes().setAll(buttonYes, buttonNo);

	        
	        inputDialog.getDialogPane().setExpandableContent(null); // Empêche l'expansion inutile
	        inputDialog.getDialogPane().setStyle("-fx-wrap-text: true;"); // Permet le retour à la ligne

	        return inputDialog.showAndWait();
	}


	
    
}
