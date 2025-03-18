package controller;





import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.Random;

public class BoardController{
	
	
		@FXML
		private Button btnBack;  //back button
		
		@FXML 
		private Pane board; //visual representation of the board
		private Game game; //game object
		private PlayerView playerView; //player view object

		
	    @FXML
	    public void initialize() {
	        // Create a list of players with one player
	        List<Player> players = new ArrayList<>();
	        players.add(new Player("Player 1"));
	        

	        // Initialize the game with the list of players
	        game = new Game(players);

	        
			// Initialize all the spaces (rectangles) on the board
			List<Rectangle> allSpaces = new ArrayList<>();
			addAllSpaces(allSpaces, board);
			
			// Initialize the spaces (rectangles) on the board
	        List<Rectangle> spaces1 = addSpaces(allSpaces,1);
	        List<Rectangle> spaces2 = addSpaces(allSpaces,2);
	        List<Rectangle> spaces3 = addSpaces(allSpaces,3);
	        List<Rectangle> spaces4 = addSpaces(allSpaces,4);
	        
	        //Create a list of all space lists
	        List<List<Rectangle>> allSpacesList = new ArrayList<>();
	        allSpacesList.add(spaces1);
	        allSpacesList.add(spaces2);
	        allSpacesList.add(spaces3);	
	        allSpacesList.add(spaces4);

            // Select a random list of spaces
	        Random random = new Random();
            List<Rectangle> selectedSpaces = allSpacesList.get(random.nextInt(allSpacesList.size())); // Get a random list of spaces
	       
            
            
			
		
	        // Initialize the player view with the current player and spaces
            playerView = new PlayerView(game.getCurrentPlayer(), javafx.scene.paint.Color.RED, selectedSpaces);
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
	    	
	    	Random rd = new Random();
	    	int steps = rd.nextInt(4)+1;
	    	
	    	
	        game.getCurrentPlayer().move(steps); // Move the player forward one to four spaces
	        playerView.updatePosition();
	        playerView.animate();
	        
	    	
	    
	    }
	    
	    private void addAllSpaces(List<Rectangle> allSpaces, Pane board){
	    	
	    	for (Node rec : board.getChildren()) {
	    		if (rec instanceof Rectangle) {
	    			allSpaces.add((Rectangle)rec);
	    		}	
			}
	    }
	    
	    private List<Rectangle> addSpaces(List<Rectangle> allSpaces, int path){
	    	List<Rectangle> spacesTmp = new ArrayList<>();
			String expectedId;
	   
	    	for (int i = 1; i<= 24; i++) {
	    		expectedId = "rec"+path+"_"+i;
	    		for (Rectangle rec : allSpaces) {
					if (expectedId.equals(rec.getId())) {
						spacesTmp.add(rec);
						System.out.println(rec);
					}
				}
	    	}
	    	return spacesTmp;
	    }
    
}