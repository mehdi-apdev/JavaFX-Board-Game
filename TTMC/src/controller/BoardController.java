package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import models.Game;
import models.JsonQuestionFactory;
import models.Player;
import models.Question;
import models.QuestionCard;
import models.QuestionCardFactory;
import models.Space;
import view.PlayerView;
import view.SpaceView;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BoardController{
	
		@FXML
		private Button btnBack;  //back button
		@FXML 
		private Pane board; //visual representation of the board
		private Game game; //game object
		private PlayerView playerView; //player view object
		@FXML
		private CheckBox musicCheckBox;
		@FXML private AnchorPane questionCard;
		@FXML
		private ImageView volumeImage;
		private Sound touchSound = new Sound();
		
		

		@FXML
		private VBox questionsContainer;
		@FXML
		private Label themeLabel;
		@FXML
		private Label questionLabel1;
		@FXML
		private Label questionLabel2;
		@FXML
		private Label questionLabel3;
		@FXML
		private Label questionLabel4;

		private List<QuestionCard> questionCards;
		private int currentCardIndex = 0;

		
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
	       
           
	       //Initialize the player view with the current player and spaces
            playerView = new PlayerView(game.getCurrentPlayer(), javafx.scene.paint.Color.RED, selectedSpaces);
	        playerView.updatePosition();
	        // Add the player's circle to the board
	        board.getChildren().add(playerView.getCircle());

	        // Add mouse click event handler to move the player
	        board.setOnMouseClicked(this::onBoardClicked);
	        
	
	        //do a shared class to avoid repetition
	        if (Main.mainSound.isMuted()== true) {
	        	musicCheckBox.setSelected(false);;
	        	Image noVolume = new Image("file:ressources/images/noVolume.png"); 
				volumeImage.setImage(noVolume);
			}else {
				musicCheckBox.setSelected(true);
				Image maxVolume = new Image("file:ressources/images/maxVolume.png"); 
				volumeImage.setImage(maxVolume);
			}
	        
	        
	        // Add key event handler to the scene
	        board.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
	        
	        
	        // Load the questions from the JSON file
	        JsonQuestionFactory jsonQuestionFactory = new JsonQuestionFactory();
	        jsonQuestionFactory.loadQuestions("ressources/questions/questions.json");
	        // Create the question cards
	        QuestionCardFactory questionCardFactory = new QuestionCardFactory(jsonQuestionFactory);
	        questionCards = questionCardFactory.createQuestionCards();
	    }

	    @FXML
	    protected void onButtonClicked(ActionEvent event) {
	        try {
	        	
	        	//Play sound
				touchSound.playMedia("click2.wav", 0.5);
	            // Load the FXML file of the new interface
	            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/menuView.fxml"));
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


		private void onBoardClicked(MouseEvent event) {
			// Move the player to a random number of steps between 1 and 4
		    Random rd = new Random();
		    int steps = rd.nextInt(4) + 1;
		
		    int currentPosition = game.getCurrentPlayer().getPosition();
		    int remainingSteps = playerView.getSpaces().size() - currentPosition - 1;
		
		    // If the number of steps is greater than the remaining steps, move to the last space
		    if (steps > remainingSteps) {
		        steps = remainingSteps;
		    }
		
		    // Move the player
		    game.getCurrentPlayer().move(steps);
		    playerView.updatePosition();
		    playerView.animate();
		}

	    //Method to add all the rectangles from the pane
	    private void addAllSpaces(List<Rectangle> allSpaces, Pane board){
	    	
	    	for (Node rec : board.getChildren()) {
	    		if (rec instanceof Rectangle) {
	    			allSpaces.add((Rectangle)rec);
	    		}	
			}
	    }
	    
	    //Method to filter rectangles
	    private List<Rectangle> addSpaces(List<Rectangle> allSpaces, int path){
	    	List<Rectangle> spacesTmp = new ArrayList<>();
			String expectedId;
	   
	    	for (int i = 1; i<= 24; i++) {
	    		expectedId = "rec"+path+"_"+i;
	    		for (Rectangle rec : allSpaces) {
					if (expectedId.equals(rec.getId())) {
						spacesTmp.add(rec);
						//System.out.println(rec);
					}
				}
	    	}
	    	return spacesTmp;
	    }
	    
	    @FXML
		protected void onChecked(ActionEvent event) {
			
			// Check the current mute status and toggle it
			if (Main.mainSound.isMuted()) { 
				Image maxVolume = new Image("file:ressources/images/maxVolume.png"); 
				volumeImage.setImage(maxVolume);
				Main.mainSound.unMuteMedia(); //method to unmute the media
			} else {
				Main.mainSound.muteMedia(); //method to mute the media
				Image noVolume = new Image("file:ressources/images/noVolume.png"); 
				volumeImage.setImage(noVolume);
			}
		}
	    
	    //Method to handle key press event to show/hide question card 'Letter
	    private void handleKeyPress(KeyEvent event) {
	        if (event.getCode() == KeyCode.P) {
	        	if(questionCard.isVisible()) {
	        		questionCard.setVisible(false);
	        		questionsContainer.setVisible(false);
	        	}else {
	        		questionCard.setVisible(true);
	        		questionsContainer.setVisible(true);
	        	}
	        }
			if (event.getCode() == KeyCode.O) {
				displayNextQuestionCard();
			}
	        
	    }
	    
	    //Method to display the next question card
	    private void displayNextQuestionCard() {
	        if (currentCardIndex < questionCards.size()) {
	            QuestionCard card = questionCards.get(currentCardIndex);
	            themeLabel.setText("Theme: " + card.getTheme().toString());
	            List<Question> questions = card.getQuestions();
	            questionLabel1.setText(questions.get(0).getTexte());
	            questionLabel2.setText(questions.get(1).getTexte());
	            questionLabel3.setText(questions.get(2).getTexte());
	            questionLabel4.setText(questions.get(3).getTexte());

	            questionCard.setVisible(true);
	            questionsContainer.setVisible(true);
	            themeLabel.setVisible(true);
	            questionLabel1.setVisible(true);
	            questionLabel2.setVisible(true);
	            questionLabel3.setVisible(true);
	            questionLabel4.setVisible(true);

	            currentCardIndex++;
	        }
	    }
		
    
}