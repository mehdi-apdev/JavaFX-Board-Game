package models;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
		alertInfo.showAndWait();
    }
	
	
	public boolean showConfirmationDialog(String title, String text) {
		alertConfirm.setHeaderText(title);
		alertConfirm.setContentText(text);
		alertConfirm.getDialogPane().setStyle("");
		alertConfirm.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alertConfirm.getButtonTypes().setAll(buttonYes, buttonCancel);

        // Afficher l'alerte et récupérer la réponse
        Optional<ButtonType> result = alertConfirm.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }
    
}
