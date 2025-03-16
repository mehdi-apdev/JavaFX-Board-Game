package controller;




import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import models.Player;
import models.Space;
import view.PlayerView;
import view.SpaceView;

public class BoardController {
	
	
		@FXML
		private Button btnBack;  //back button
		
		@FXML 
		private Pane board; //visual representation of the board
		private Game game; //game object
		private PlayerView playerView; //player view object
		@FXML private Rectangle first; //rectangle object
		@FXML private Rectangle second; //rectangle object
		@FXML private Rectangle third; //rectangle object
		@FXML private Rectangle fourth; //rectangle object
		
	    @FXML
	    public void initialize() {
	        // Create a list of players with one player
	        List<Player> players = new ArrayList<>();
	        players.add(new Player("Player 1"));

	        // Initialize the game with the list of players
	        game = new Game(players);

	        // Initialize the spaces (rectangles) on the board
	        List<Rectangle> spaces = new ArrayList<>();
	        // Add your rectangles to the spaces list here
			spaces.add(first);
			spaces.add(second);
			spaces.add(third);
			spaces.add(fourth);
			
	        

	        // Initialize the player view with the current player and spaces
	        playerView = new PlayerView(game.getCurrentPlayer(), javafx.scene.paint.Color.RED, spaces);
	        playerView.updatePosition();
	        // Add the player's circle to the board
	        board.getChildren().add(playerView.getCircle());

	        // Add mouse click event handler to move the player
	        board.setOnMouseClicked(this::onBoardClicked);
	    }

	    @FXML
	    protected void onButtonClicked(ActionEvent event) {
	        try {
	            // Load the FXML file of the new interface
	            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/menuView.fxml"));
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

	    private void onBoardClicked(MouseEvent event) {
	        // Move the player forward one space
	        game.getCurrentPlayer().move(1);
	        playerView.updatePosition();
	        playerView.animate();
	    }
	}


