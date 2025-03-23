package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import controller.PlayerChoiceViewController;
import application.Main;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
import javafx.util.Duration;
import models.Game;
import models.JsonQuestionFactory;
import models.Player;
import models.Question;
import models.QuestionCard;
import models.QuestionCardFactory;
import view.PlayerView;


	   
/**
 * Controller class for the game board interface.
 * Manages player movement, question cards display, and game interactions.
 */
public class BoardController {

    
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final int MAX_DICE_VALUE = 4;
    private static final int MIN_DICE_VALUE = 1;
    private static final int ANIMATION_DURATION = 1;
    private static final String CLICK_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.5;
    private static int nbPlayers = 0;
    private static List<Player>players;
    private static List<Label> playersNames;
    private static List<Label> playersHints;
    private Timeline timeline; 
   
 // Sound effect for button interactions
    private final Sound touchSound = new Sound();
    private final Sound timerSound = new Sound();
    
    @FXML private Button btnBack, validerButton;
    @FXML private Pane board, playersContainer;
    @FXML private CheckBox musicCheckBox;
    @FXML private AnchorPane questionCard;
    @FXML private ImageView volumeImage;
    @FXML private VBox questionsContainer, questionBox;
    @FXML private Label themeLabel, questionLabel1, questionLabel2, questionLabel3, questionLabel4;
    @FXML private Label namePlayer1, namePlayer2, namePlayer3, namePlayer4, timerLabel;
    @FXML private Label playerHint1, playerHint2, playerHint3, playerHint4;
    private Game game;
    private PlayerView playerView;
    private Sound sound = new Sound();
    private List<QuestionCard> questionCards;
    private int currentCardIndex = 0;
    private Random random = new Random();
    
    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        initializeGame();
        initializeBoard();
        initializeSound();
        initializeEventHandlers();
        loadQuestions();
    }
    
    /**
     * Initializes the game with a single player.
     */
    private void initializeGame() {
    	initializePlayersName();
        players = new ArrayList<>();
        
        for(int i = 1; i<= nbPlayers ; i++) {
        	players.add(new Player(PlayerChoiceViewController.getSelectedListPlayersNames().get(i-1)));
        	System.out.println(players.get(i-1).getName());
        }
        initializeHints();
        
        game = new Game(players);
    }
    
    /**
     * Initializes the game board with spaces and player representation.
     */
    private void initializeBoard() {
        List<Rectangle> allSpaces = new ArrayList<>();
        addAllSpaces(allSpaces, board);
        
        List<List<Rectangle>> allSpacesList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            allSpacesList.add(addSpaces(allSpaces, i));
        }
        
        //for (int i = 0;i <nbPlayers; i++ ) {
        	
	        List<Rectangle> selectedSpaces = allSpacesList.get(random.nextInt(allSpacesList.size()));
	        
	        javafx.scene.paint.Paint playerColor = PlayerChoiceViewController.getSelectedColor(); 
	        
	        playerView = new PlayerView(game.getCurrentPlayer(), 
	                playerColor != null ? playerColor : javafx.scene.paint.Color.RED, 
	                selectedSpaces);
	        System.out.println("Liste des joueurs"+game.getPlayers());
	        playerView.updatePosition();
	        board.getChildren().add(playerView.getCircle());
        //}
    }
    
    /**
     * Initializes sound settings based on the current mute state.
     */
    private void initializeSound() {
        boolean isMuted = Main.mainSound.isMuted();
        musicCheckBox.setSelected(!isMuted);
        volumeImage.setImage(new Image(isMuted ? VOLUME_OFF_IMAGE : VOLUME_ON_IMAGE));
    }
    
    private void initializeHints() {
    	
    	playersHints = new ArrayList<>();
    	playersHints.add(playerHint1);
    	playersHints.add(playerHint2);
    	playersHints.add(playerHint3);
    	playersHints.add(playerHint4);
    	
    	int i = 1 ;
    	for (Label hint: playersHints) {
    		
    		if(i <= PlayerChoiceViewController.getSelectedListPlayersNames().size()) {
    			hint.setText(players.get(i-1).getHint()+" left(s)");
    		}else{
    			hint.setText("/");
    		}
    		i++;
		}
    }
    
    
    /**
     * Initializes Players'names
     */
    private int initializePlayersName() {
    	playersNames = new ArrayList<>();
    	playersNames.add(namePlayer1);
    	playersNames.add(namePlayer2);
    	playersNames.add(namePlayer3);
    	playersNames.add(namePlayer4);
    	
    	int i = 1 ;
    	for (Label playerName : playersNames) {
    		
    		if(i <= PlayerChoiceViewController.getSelectedListPlayersNames().size()) {
    			playerName.setText(PlayerChoiceViewController.getSelectedListPlayersNames().get(i-1));
    			nbPlayers++;
    		}else {
    			playerName.setText("/");
    		}
    	
    		i++;
		}
    	
    	return nbPlayers;
    }
    
    /**
     * Sets up event handlers for user interactions.
     */
    private void initializeEventHandlers() {
        board.setOnMouseClicked(this::onBoardClicked);
        board.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }
    
    /**
     * Loads question cards from the JSON file.
     */
    private void loadQuestions() {
        JsonQuestionFactory jsonQuestionFactory = new JsonQuestionFactory();
        jsonQuestionFactory.loadQuestions("ressources/questions/questions.json");
        QuestionCardFactory questionCardFactory = new QuestionCardFactory(jsonQuestionFactory);
        questionCards = questionCardFactory.createQuestionCards();
    }
    
    /**
     * Handles the back button click event.
     * Returns to the main menu.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonClicked(ActionEvent event) {
    	 touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
    	 boolean result;
    	 result = showConfirmationDialog("YOUR PROGRESS WILL BE LOST", "Are you sure you want to leave the game ?");
         if (result) {
        	 navigateToView("../view/menuView.fxml", event);
        	 quitGame();
         }
    }
    
    /**
     * Handles board click events to move the player.
     * Moves the player a random number of steps (1-4).
     * 
     * @param event The mouse event
     */
    private void onBoardClicked(MouseEvent event) {
        int steps = random.nextInt(MAX_DICE_VALUE) + MIN_DICE_VALUE;
        
        int currentPosition = game.getCurrentPlayer().getPosition();
        int remainingSteps = playerView.getSpaces().size() - currentPosition - 1;
        
        if (steps > remainingSteps) {
            steps = remainingSteps;
        }
        
        game.getCurrentPlayer().move(steps);
        playerView.updatePosition();
        playerView.animate();
    }
    
    /**
     * Collects all Rectangle objects from the board pane.
     * 
     * @param allSpaces List to store the rectangles
     * @param board The board pane containing rectangles
     */
    private void addAllSpaces(List<Rectangle> allSpaces, Pane board) {
        for (Node node : board.getChildren()) {
            if (node instanceof Rectangle) {
                allSpaces.add((Rectangle) node);
            }
        }
    }
    
    /**
     * Filters rectangles for a specific path.
     * 
     * @param allSpaces List of all rectangles
     * @param path Path identifier (1-4)
     * @return List of rectangles for the specified path
     */
    private List<Rectangle> addSpaces(List<Rectangle> allSpaces, int path) {
        List<Rectangle> spacesTmp = new ArrayList<>();
        
        for (int i = 1; i <= 24; i++) {
            String expectedId = "rec" + path + "_" + i;
            for (Rectangle rec : allSpaces) {
                if (expectedId.equals(rec.getId())) {
                    spacesTmp.add(rec);
                    break;
                }
            }
        }
        
        return spacesTmp;
    }
    
    /**
     * Handles the music checkbox toggle event.
     * Mutes or unmutes the game sound.
     * 
     * @param event The action event
     */
    @FXML
    protected void onChecked(ActionEvent event) {
    	
    	if (touchSound.isPlaying()) {
    		
    		return;
    	}
    	
        if (Main.mainSound.isMuted()) {
            volumeImage.setImage(new Image(VOLUME_ON_IMAGE));
            Main.mainSound.unMuteMedia();
        } else {
            Main.mainSound.muteMedia();
            volumeImage.setImage(new Image(VOLUME_OFF_IMAGE));
        }
    }
    
    /**
     * Handles keyboard input events.
     * P key toggles question card visibility.
     * O key displays the next question card.
     * 
     * @param event The key event
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            toggleQuestionCardVisibility();
        } else if (event.getCode() == KeyCode.O) {
            displayNextQuestionCard();
            playTimer();
        }
        
        if (event.getCode() == KeyCode.N) {
           //displayPlayerPane();
        }
    }
    
    /**
     * Toggles the visibility of the question card.
     */
    private void toggleQuestionCardVisibility() {
        boolean isVisible = questionCard.isVisible();
        questionCard.setVisible(!isVisible);
        questionsContainer.setVisible(!isVisible);
        timerSound.stopMedia();
        musicCheckBox.setDisable(false);
        initializeSound();
      
        
        if (!isVisible) {
            playTransition(questionCard, false);
        }
    }
    
    /**
     * Displays the next question card with animation.
     */
    private void displayNextQuestionCard() {
        if (currentCardIndex < questionCards.size()) {
            QuestionCard card = questionCards.get(currentCardIndex);
            themeLabel.setText("Theme: " + card.getTheme().toString());
            
            List<Question> questions = card.getQuestions();
            Label[] labels = {questionLabel1, questionLabel2, questionLabel3, questionLabel4};
            
            for (int i = 0; i < Math.min(questions.size(), labels.length); i++) {
                labels[i].setText(questions.get(i).getTexte());
                labels[i].setVisible(true);
            }
            
            questionCard.setVisible(true);
            questionsContainer.setVisible(true);
            themeLabel.setVisible(true);
            
            SequentialTransition sequentialTransition = new SequentialTransition();
            sequentialTransition.getChildren().add(playTransitionLabel(themeLabel));
            
            for (Label label : labels) {
                sequentialTransition.getChildren().add(playTransitionLabel(label));
            }
            
            sequentialTransition.play();
            currentCardIndex++;
        }
    }
    
    private void displayPlayerPane() {
    	
    	        // Lancer l'animation et définir la visibilité à false à la fin
    	        playTransitionPlayerPane(playersContainer).setOnFinished(e -> {
    	            playersContainer.setVisible(false);
    	        });
    }
    /**
     * Creates and plays a scale transition animation for a pane.
     * 
     * @param container The pane to animate
     * @param isOpen Whether the animation is for opening (true) or closing (false)
     */
    private void playTransition(Pane container, boolean isOpen) {
        int fromScale = isOpen ? 1 : 0;
        int toScale = isOpen ? 0 : 1;
        
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(ANIMATION_DURATION), container);
        scaleTransition.setFromX(fromScale);
        scaleTransition.setFromY(fromScale);
        scaleTransition.setToX(toScale);
        scaleTransition.setToY(toScale);
        
        sound.playMedia("zoom.wav", 0.5);
        scaleTransition.play();
    }
    
    /**
     * Creates a scale transition animation for a label.
     * 
     * @param container The label to animate
     * @return The configured scale transition
     */
    private ScaleTransition playTransitionLabel (Label container) {
	    
    	//Create a ScaleTransition to enlarge the Pane
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), container);
        scaleTransition.setFromX(0); 
        scaleTransition.setFromY(0); 
        scaleTransition.setToX(1);   
        scaleTransition.setToY(1);
        sound.playMedia("nextQst.wav", 0.3);
        sound.resetMedia();

        return scaleTransition;
    }
    
    private TranslateTransition playTransitionPlayerPane(Pane container) {
    	
    	
    	double setFromY = container.getLayoutY(), setToY = -100;
    	
    	if(container.isVisible() == false) {
    		setFromY = -100;
    		setToY = container.getLayoutY();
    		container.setVisible(true);
    	}
    	
    	TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(2));
        transition.setNode(container);
        transition.setFromY(setFromY); // start position
        transition.setToY(setToY); //Final Position
 
      
        //Start transition
        transition.play();
        return transition;
    }
    
    /**
     * Handles the validate button click event.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonValiderClicked(ActionEvent event) {
        sound.playMedia("click2.wav", 0.5);
    }
    
 // Method to show confirmation alerts
    private boolean showConfirmationDialog(String title, String text) {
        // Créer un alert de type CONFIRMATION
        Alert alert = new Alert(AlertType.CONFIRMATION);
        
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.getDialogPane().setStyle("");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonCancel);

        // Afficher l'alerte et récupérer la réponse
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonYes;
    }
    
    private void navigateToView(String fxmlPath, ActionEvent event) {
        try {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane root = fxmlLoader.load();
            
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            scene.getStylesheets().add(getClass().getResource("../application/application.css").toExternalForm());
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void playTimer() {
        // If a timer already exists, stop it and clean up
        if (timeline != null) {
            timeline.stop();
        }

        int[] duration = {60}; // Reset the duration
        timerSound.resetMedia(); // Reset the media sound

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                if (duration[0] > 0) {
                    timerLabel.setText((duration[0]--) + ""); // Update the label
                } else {
                    timerLabel.setText("End");
                    //timerSound.playMedia("timerEnd.wav", SOUND_VOLUME);
                }
            })
        );

        timeline.setCycleCount(duration[0] + 1); // Set the cycle count
        timeline.play(); // Start the new timer

        Main.mainSound.muteMedia(); // Mute the main sound
        initializeSound(); // Reset the sound parameters
        timerSound.playMedia("timerMusic.mp3", SOUND_VOLUME); // Play the timer music
        timerSound.loop(); // Loop the music
        musicCheckBox.setDisable(true); // Disable the checkbox
    }
    private void quitGame() {
    	 players.clear();
    	 playersHints.clear();
    	 playersNames.clear();
    	 timerSound.stopMedia();
    	 nbPlayers = 0;
    }

}
