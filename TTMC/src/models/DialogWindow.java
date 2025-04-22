package models;

import javafx.util.Duration;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
/*
 * This class is used to create dialog windows for information, confirmation, and input.
 * It uses JavaFX's Alert class to display the dialogs.
 * */
public class DialogWindow {
	private Alert alertInfo;
	private Alert alertConfirm;
	private static final String ICON_PATH = "file:ressources/images/logoGj.png";
	private ImageView imageView;
	
	// Constructor
	public DialogWindow() {
		  alertInfo = new Alert(AlertType.INFORMATION);
		  alertConfirm = new Alert(AlertType.CONFIRMATION);
	}
	
	/*
	 * * This method is used to show an information alert.
	 * 
	 * @param title The title of the alert window.
	 * 
	 * @param message The message to be displayed in the alert window.
	 */
	public void showAlert(String title, String message) {
		
		/*Image in the dialog box
        Loading the image*/
		 // Initialiser l'ImageView
	    imageView = new ImageView();
        imageView.setImage(new Image(ICON_PATH));
        // Adjust size
        imageView.setFitHeight(50);
        imageView.setFitWidth(90);
        imageView.setPreserveRatio(false);
        //Adds the ImageView (containing the image) as a graphic element to the dialog box.
        alertInfo.setGraphic(imageView);
        
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
	
	/*
	 * This method is used to show a confirmation alert.
	 * 
	 * @param title The title of the alert window.
	 * 
	 * @param text The message to be displayed in the alert window.
	 * 
	 * @return true if the user clicked "Ok", false otherwise.
	 */
	public boolean showConfirmationDialog(String title, String text) {
		imageView = new ImageView();
        imageView.setImage(new Image(ICON_PATH));
        imageView.setFitHeight(50);
        imageView.setFitWidth(90);
        imageView.setPreserveRatio(false);
        alertConfirm.setGraphic(imageView);
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

	
	/*
	 * This method is used to show an input dialog.
	 * 
	 * @param title The title of the alert window.
	 * 
	 * @param message The message to be displayed in the alert window.
	 * 
	 * @return The user input as a String, or an empty Optional if the user clicked
	 * "Cancel".
	 */
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
