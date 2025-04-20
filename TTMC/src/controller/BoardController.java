package controller;


import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import application.Main;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Bonus1;
import models.Bonus2;
import models.Bonus3;
import models.DialogWindow;
import models.EducationQuestionFactory;
import models.EntertainmentQuestionFactory;
import models.Game;
import models.ImprobableQuestionFactory;
import models.InformaticsQuestionFactory;
import models.Malus1;
import models.Bonus4;
import models.MysteryState;
import models.QuestionLoader;
import models.Player;
import models.Question;
import models.QuestionCard;
import models.QuestionCardFactory;
import models.QuestionFactory;
import models.Topic;
import view.PlayerView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Controller class for the game board interface.
 * Manages player movement, question cards display, and game interactions.
 */
public class BoardController {
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final String MALUS_GIF = "file:ressources/images/georgeMalusGif.gif";
    private static final String BONUS_GIF = "file:ressources/images/georgeBonusGif.gif";
    private static final int MAX_DICE_VALUE = 4;
    private static final int MIN_DICE_VALUE = 1;
    private static final double ANIMATION_DURATION = 0.6;
    private static final String CLICK_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.1;
    private static final double USE_COMPUTED_SIZE = -1;
    
    private static int nbPlayers = 0;
    private static List<Player> players;
    private static List<Player> standingsPlayers;
    private static List<Label> playersNames;
    private static List<Label> playersHints;
    private static List<Label> playersPos;
    private Timeline timeline; 
    private static Game game;
    private PlayerView playerView;
    private List<QuestionCard> questionCards;
    private int currentCardIndex = 0;
    private Random random = new Random();
    private MysteryState mysteryState;
    
    //Windows for aletts
    private DialogWindow dialog = new DialogWindow();
    
    @FXML private Button btnBack, validerButton;
    @FXML private Pane board, playersContainer, georgeBonusGifPane, standingsPane;
    @FXML private AnchorPane questionCard;
    @FXML private ImageView volumeImage, timerImage, bonusMalusImage;
    @FXML private VBox questionsContainer, questionBox;
    @FXML private Label themeLabel, questionLabel1, questionLabel2, questionLabel3, questionLabel4,  questionSelectionneeLabel;
    @FXML private Label namePlayer1, namePlayer2, namePlayer3, namePlayer4, timerLabel;
    @FXML private Label playerHint1, playerHint2, playerHint3, playerHint4;
    @FXML private RadioButton response1, response2, response3, response4;
    @FXML private ToggleGroup reponse;
    @FXML private Circle circlePlayer1, circlePlayer2, circlePlayer3, circlePlayer4;
	@FXML private Label scorePlayer1, scorePlayer2, scorePlayer3, scorePlayer4;
	@FXML private Label streakPlayer1, streakPlayer2, streakPlayer3, streakPlayer4, playerPos1, playerPos2, playerPos3, playerPos4;
	

	private static final String purpleStr = "51244c";
	private static final String yellowStr= "c0721e";
	private static final String blueStr = "587a96";
	private static final String greenStr = "89932b";
	private static final String redStr = "0x8a1515ff";
	private static final String whiteStr = "ffffff";
	private static boolean isStreak;
	private  static int pos = 1;
	
	private List<Label> playersScores;
	private static List<Integer> positions;
	private List<Label> playersStreaks;
	private List<QuestionCard> usedQuestionCards;
    private List<PlayerView> playerViews;
    
    
    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
    	usedQuestionCards = new ArrayList<>();
        initializeGame();
        initializeBoard();
        initializeSound();
        initializeScoreAndStreak();
        initializePlayersPos();
       // initializeEventHandlers();
        loadQuestions();
        
		Platform.runLater(() -> {
			// Display the first question card
			displayQuestionCardBasedOnPosition();
		});
    }
    
    /**
     * Initializes the game with players.
     */
    private void initializeGame() {
        initializePlayersName();
      
        players = new ArrayList<>();
       
        
        for(int i = 1; i <= nbPlayers; i++) {
            players.add(new Player(PlayerChoiceViewController.getSelectedListPlayersNames().get(i-1)));
            
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
        
     // Initialiser les couleurs des rectangles
        initializeRectangleColors(allSpaces);
        
        List<List<Rectangle>> allSpacesList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            allSpacesList.add(addSpaces(allSpaces, i));
        }
        
        
        List<Circle> allCircles = new ArrayList<>();
        allCircles.add(circlePlayer1);
        allCircles.add(circlePlayer2);
        allCircles.add(circlePlayer3);
        allCircles.add(circlePlayer4);
        
        
        playerViews = new ArrayList<>();
        List<Paint> selectedColors = PlayerChoiceViewController.getSelectedColors();
        
        
        for (int i = 0; i < nbPlayers; i++) {
        	List<Rectangle> selectedSpaces = allSpacesList.get(i);
        	//System.out.println(allSpacesList.get(i));
            Paint playerColor = selectedColors.get(i); 
            allCircles.get(i).setVisible(true);
            allCircles.get(i).setFill(playerColor);
            playerView = new PlayerView(players.get(i), allCircles.get(i), selectedSpaces);
            playerView.updatePosition();
            playerViews.add(playerView);
            
            System.out.println("Player " + (i + 1) + " Circle: " + allCircles.get(i).getId());
        }
           //circlePlayer1.setVisible(true);
            //circlePlayer1.setFill(playerColor);
            //playerView = new PlayerView(game.getCurrentPlayer(), circlePlayer1, selectedSpaces);
           //playerView.updatePosition();
           // board.getChildren().add(playerView.getCircle());
            //game.nextPlayer();
        //}
        
       // board.getChildren().add(questionCard);
       //board.getChildren().add(playerView.getCircle());
    }
    
    private void initializeRectangleColors(List<Rectangle> allSpaces) {
        Color[] colors = { Color.web(purpleStr), Color.web(blueStr), Color.web(greenStr), Color.web(yellowStr)};
        int colorIndex = 0;

        for (Rectangle rect : allSpaces) {
            rect.setFill(colors[colorIndex]);
            rect.getStyleClass().add("rectangle"); // Add the CSS style class
            colorIndex = (colorIndex + 1) % colors.length;
        }
    }
    
    /**
     * Initializes sound settings based on the current mute state.
     */
    private void initializeSound() {
        boolean isMuted = Main.getMainSound().isMuted();
        volumeImage.setImage(new Image(isMuted ? VOLUME_OFF_IMAGE : VOLUME_ON_IMAGE));
    }
    
    /**
     * Initializes player hints display.
     */
    private void initializeHints() {
        playersHints = new ArrayList<>();
        playersHints.add(playerHint1);
        playersHints.add(playerHint2);
        playersHints.add(playerHint3);
        playersHints.add(playerHint4);
        
        int i = 1;
        for (Label hint: playersHints) {
            if(i <= PlayerChoiceViewController.getSelectedListPlayersNames().size()) {
                hint.setText("Hints: "+players.get(i-1).getHint()+" left(s)");
            } else {
                hint.setText("");
            }
            i++;
        }
    }
    
    /**
     * Initializes player names display and counts total players.
     * @return Number of players
     */
    private int initializePlayersName() {
        playersNames = new ArrayList<>();
        playersNames.add(namePlayer1);
        playersNames.add(namePlayer2);
        playersNames.add(namePlayer3);
        playersNames.add(namePlayer4);
        
        int i = 1;
        for (Label playerName : playersNames) {
            if(i <= PlayerChoiceViewController.getSelectedListPlayersNames().size()) {
                playerName.setText(PlayerChoiceViewController.getSelectedListPlayersNames().get(i-1));
                nbPlayers++;
            } else {
                playerName.setText("-----");
            }
            i++;
        }
        
        return nbPlayers;
    }
    
    private void initializePlayersPos() {
    	playersPos = new ArrayList<>();
    	positions = new ArrayList<>();
    	standingsPlayers = new ArrayList<>();
    	playersPos.add(playerPos1);
    	playersPos.add(playerPos2);
    	playersPos.add(playerPos3);
    	playersPos.add(playerPos4);
    	
		for (int i = 0; i < players.size(); i++) {
			playersPos.get(i).setVisible(true);
			positions.add(i);
	        playersPos.get(i).setText(1+"th");
		}
    }
    
    /**
     * Initializes event handlers and displays the first question card.
     */

        
private void loadQuestions() {
    QuestionLoader loader = new QuestionLoader();
    loader.loadQuestions("ressources/questions/questions.json");

    Map<Topic, List<Question>> questionsByTopic = new HashMap<>();
    for (Topic topic : Topic.values()) {
        QuestionFactory factory = loader.getFactories().get(topic);
        if (factory != null) {
            switch (topic) {
                case IMPROBABLE:
                    questionsByTopic.put(topic, ((ImprobableQuestionFactory) factory).getQuestions());
                    break;
                case INFORMATICS:
                    questionsByTopic.put(topic, ((InformaticsQuestionFactory) factory).getQuestions());
                    break;
                case ENTERTAINMENT:
                    questionsByTopic.put(topic, ((EntertainmentQuestionFactory) factory).getQuestions());
                    break;
                case EDUCATION:
                    questionsByTopic.put(topic, ((EducationQuestionFactory) factory).getQuestions());
                    break;
            }
        }
    }

    QuestionCardFactory questionCardFactory = new QuestionCardFactory();
    questionCards = questionCardFactory.createQuestionCards(questionsByTopic);
    Collections.shuffle(questionCards);
}

    
    /**
     * Handles the back button click event.
     * Returns to the main menu after confirmation.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonClicked(ActionEvent event) {
    	MenuController.getTouchSound().playMedia(CLICK_SOUND, SOUND_VOLUME);
        boolean result = dialog.showConfirmationDialog("YOUR PROGRESS WILL BE LOST", "Are you sure you want to leave the game?");
        if (result) {
            navigateToView("../view/menuView.fxml", event);
            quitGame();
        }
    }
    
    /**
     * Handles keyboard input events.
     * 
     * @param event The key event
     *
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            toggleQuestionCardVisibility();
        }  else if (event.getCode() == KeyCode.A) {
            movePlayerRandomSteps();
        }
    }
    **/
    
    /**
     * Moves the player a random number of steps.
     *
    private void movePlayerRandomSteps() {
        int steps = random.nextInt(MAX_DICE_VALUE) + MIN_DICE_VALUE;
        
        
        Player currentPlayer = game.getCurrentPlayer();
        PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
        
        
        int currentPosition = currentPlayer.getPosition();
        int remainingSteps = currentPlayerView.getSpaces().size() - currentPosition - 1;
        
        
        

        if (steps > remainingSteps) {
            steps = remainingSteps;
        }

        currentPlayer.move(steps);
        //currentPlayerView.updatePosition();
        currentPlayerView.animateMovement(steps);

        displayQuestionCardBasedOnPosition();
        game.nextPlayer();
       
        System.out.println(game.getCurrentPlayer().getName() + " moved " + steps + " spaces.");
    }
    */
    
    /**
     * Toggles the visibility of the question card.
     */
    private void toggleQuestionCardVisibility() {
        boolean isVisible = questionCard.isVisible();
        questionCard.setVisible(!isVisible);
        questionsContainer.setVisible(!isVisible);
        MenuController.getTimerSound().stopMedia();
        //initializeSound();
        
        if (!isVisible) {
            playTransition(questionCard, false);
        }
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
    
    



@FXML
private void onHintButtonClicked(ActionEvent event) {
    Player currentPlayer = game.getCurrentPlayer();
    if (currentPlayer.getHint() > 0 && !currentPlayer.hasUsedHintThisRound()) {
        // Get the current question from the validerButton
        Question currentQuestion = (Question) validerButton.getUserData();
        if (currentQuestion == null) {
            dialog.showAlert("No question active", "You can only use a hint when a question is displayed.");
            return;
        }

        // Find all incorrect responses
        RadioButton[] responseButtons = {response1, response2, response3, response4};
        List<RadioButton> incorrectResponses = new ArrayList<>();

        for (RadioButton rb : responseButtons) {
            if (rb.isVisible() && rb.getUserData() != null) {
                int responseIndex = (int) rb.getUserData();
                if (!currentQuestion.isCorrectResponse(responseIndex)) {
                    incorrectResponses.add(rb);
                }
            }
        }

        // If there are incorrect options, remove one randomly
        if (!incorrectResponses.isEmpty()) {
            RadioButton toDisable = incorrectResponses.get(random.nextInt(incorrectResponses.size()));
            toDisable.setDisable(true);
            toDisable.setStyle("-fx-opacity: 0.5; -fx-text-fill: gray;");

            // Use hint
            MenuController.getSecondarySound().playMedia("hint.wav", 0.1);
            currentPlayer.useHint();
            currentPlayer.setUsedHintThisRound(true);
            dialog.showAlert("Hint used", "One incorrect option has been removed.");
        } else {
            dialog.showAlert("Hint unavailable", "No incorrect options to remove.");
        }
        
    } else {
        dialog.showAlert("No hints left", "You have no hints left or have already used a hint this round.");
    }
    updateHintsDisplay();
    
}


private void updateHintsDisplay() {
    for (int i = 0; i < players.size(); i++) {
        playersHints.get(i).setText("Hints : "+players.get(i).getHint() + " left(s)");
        
    }
}


    
   
    /**
     * Handles the music image toggle event.
     * Mutes or unmutes the game sound.
     * 
     * @param event The action event
     */

    @FXML    
    protected void onVolumeClicked(MouseEvent event) {
    	if (MenuController.getTimerSound().isPlaying()) {
    		return;
    	}
    	
        if (Main.getMainSound().isMuted()) {
            volumeImage.setImage(new Image(VOLUME_ON_IMAGE));
            Main.getMainSound().unMuteMedia();
        } else {
            Main.getMainSound().muteMedia();
            volumeImage.setImage(new Image(VOLUME_OFF_IMAGE));
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
        
        MenuController.getSecondarySound().playMedia("zoom.wav", SOUND_VOLUME);
        scaleTransition.play();
    }
    
    /**
     * Creates a scale transition animation for a label.
     * 
     * @param label The label to animate
     * @return The configured scale transition
     */
    private ScaleTransition playTransitionLabel(Label label) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(ANIMATION_DURATION), label);
        scaleTransition.setFromX(0);
        scaleTransition.setFromY(0);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        return scaleTransition;
    }
    
    

    
    /**
     * Handles the validate button click to check the selected answer.
     * 
     * @param event The action event
     */
    @FXML
    private void onButtonValiderClicked(ActionEvent event) {
    	
    	
    	
        Question currentQuestion = (Question) validerButton.getUserData();

        if (reponse.getSelectedToggle() == null) {
            dialog.showAlert("No answer selected", "Please select an answer before validating.");
            return;
        }

        int selectedResponseIndex = (int) reponse.getSelectedToggle().getUserData();
        int difficulty = currentQuestion.getDifficulty();
        boolean isCorrect = currentQuestion.isCorrectResponse(selectedResponseIndex);
        Player currentPlayer = game.getCurrentPlayer();

        if (isCorrect) {
            stopTimer();
            MenuController.getSecondarySound().playMedia("good.wav", SOUND_VOLUME);
            
            // Increase score based on difficulty
            currentPlayer.increaseScore(50 + difficulty * 50);
            currentPlayer.increaseStreak();
            int stepsToMove = currentQuestion.getDifficulty();
            
            dialog.showAlert("Correct answer!", "You move forward " + stepsToMove + " space(s).");
            
            if (currentPlayer.hasThreeStreaks()) {
                dialog.showAlert("Three in a row!", "You have answered 3 questions correctly in a row!");
                currentPlayer.resetStreak();
                
                int avg = currentPlayer.averageScore();
                if (avg < 101) {
                    currentPlayer.increaseScore(30);
                } else if (avg < 151) {
                    currentPlayer.increaseScore(50);
                } else if (avg < 201) {
                    currentPlayer.increaseScore(60);
                } else {
                    currentPlayer.increaseScore(70);
                }
                
                stepsToMove += 2;
                displayGif(BONUS_GIF);
                MenuController.getSecondarySound().playMedia("bonus.mp3", SOUND_VOLUME);
            }

            movePlayerForward(stepsToMove);
        } else {
        	stopTimer();
        	MenuController.getSecondarySound().playMedia("wrong.mp3",SOUND_VOLUME);
        	currentPlayer.decreaseScore(40);
            currentPlayer.resetStreak();
            dialog.showAlert("Wrong answer!", "Better luck next time!");
            stopTimer();
        }
        stopTimer();
        updateScoreAndStreakDisplay();
        toggleQuestionCardVisibility();

        // Get next player before showing the turn message
        nextPlayer();

		         
        volumeImage.setDisable(false);
        Image img = volumeImage.getImage();
        if (img.getUrl().equals(VOLUME_ON_IMAGE)) {
            Main.getMainSound().unMuteMedia();
        }
    }

    
    /**
     * Moves the player forward a specified number of steps.
     * 
     * @param steps Number of steps to move forward
     */
    private void movePlayerForward(int steps) {
    	
    	Player currentPlayer = game.getCurrentPlayer();
        PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());

        int currentPosition = currentPlayer.getPosition();
        int remainingSteps = currentPlayerView.getSpaces().size() - currentPosition - 1;

        if (steps > remainingSteps) {
            steps = remainingSteps;
        }

        currentPlayer.move(steps);
        //currentPlayerView.updatePosition();
        currentPlayerView.animateMovement(steps);
    }
    
    /**
     * Displays a question card based on the player's current position.
     * The theme is determined by the color of the rectangle where the player is located.
     */
    private void displayQuestionCardBasedOnPosition() {
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> {
            Platform.runLater(() -> {
                // Get current player and position information
                Player currentPlayer = game.getCurrentPlayer();
                int playerIndex = game.getCurrentPlayerIndex();
                PlayerView currentPlayerView = playerViews.get(playerIndex);
                int position = currentPlayer.getPosition();

                // Debugging
                System.out.println("Current Player: " + currentPlayer.getName());
                System.out.println("Current Position: " + position);

                // Check for valid position
                if (position >= currentPlayerView.getSpaces().size()) {
                    System.out.println("Invalid position: " + position);
                    return;
                }

                // Get rectangle at current position
                Rectangle currentRectangle = currentPlayerView.getSpaces().get(position);
                String fillColor = currentRectangle.getFill().toString();
                
                // Check if game is finished (only one player left)
                if (game.getPlayers().size() <= 1) {
                    handleGameEnd();
                    return;
                }
              
                // Check if player reached the finish line
                if (fillColor.equalsIgnoreCase(whiteStr) || currentRectangle.getStyleClass().contains("last")) {
                    handlePlayerFinish(playerIndex);
                    return;
                }
                
                // Check if player landed on a malus space
                if (currentRectangle.getStyleClass().contains("mystery")) {
                	
                	int randomMystery = ThreadLocalRandom.current().nextInt(1, 6);
                	
                	switch (randomMystery) {
                		case 1:
                			handlePlayerMovingBack();
                			break;
                		case 2:
                			handleSwitchPlayers();
                			break;
                		case 3:
                			handlePlayerBonusForward();
                			break;
                		case 4 :
                			handlePlayerHintBonus();
                			break;
                		case 5 :
                			handleFreezeOpponent();
							break;
                	}
                    
                    return;
                }
                
                // Display appropriate question card based on the space color
                displayQuestionCardByColor(fillColor);
            });
        });
        pause.play();
    }

    /**
     * Handles the end of the game when only one player remains.
     */
    private void handleGameEnd() {
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> {
            Platform.runLater(() -> {
                standingsPane.setVisible(true);
                playTransition(standingsPane, false);
            });
        });
        
        dialog.showAlert("End of the game", "All players have finished the game!");
        pause.play();
    }

    /**
     * Handles a player reaching the finish line.
     * @param playerIndex The index of the player who finished
     */
    private void handlePlayerFinish(int playerIndex) {
        standingsPlayers.add(game.getCurrentPlayer());
        pos++;
        
        // Remove the player from the game and update UI
        removePlayerFromGame(playerIndex);
        updatePlayerPostions();
        
        // Change to next player
        nextPlayer();
        dialog.showAlert("Congrats", "You have reached the end of the game!");
        
        // Wait for animation and proceed to next player's turn
        waitForAnimation();
    }
    

    private void handleFreezeOpponent() {
    	mysteryState = new Bonus3();
        Player currentPlayer = game.getCurrentPlayer();
        PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
        mysteryState.executeMystery(game, currentPlayer, currentPlayerView);
        nextPlayer();
    }

    
    private void handlePlayerHintBonus() {
    	mysteryState = new Bonus2();
		Player currentPlayer = game.getCurrentPlayer();
		PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
		mysteryState.executeMystery(game ,currentPlayer, currentPlayerView);
	

		// Passe au joueur suivant SANS déclencher une autre vérification de case
		nextPlayer();

		// Important : utilisez une pause pour attendre la fin de l'animation
		// sans déclencher de nouveau displayQuestionCardBasedOnPosition
		PauseTransition waitTransition = new PauseTransition(Duration.seconds(ANIMATION_DURATION + 0.2));
		waitTransition.setOnFinished(e -> {
			// Ne rien faire ici - juste attendre que l'animation se termine
			// avant de permettre d'autres actions
			dialog.showAlert("Bonus Square!", "You have received a hint bonus.");
			updateHintsDisplay();

		});
		waitTransition.play();
    }

    /**
     * Handles a player landing on a malus (penalty) space.
     */
    private void handlePlayerMovingBack() {
    	mysteryState = new Malus1();
        Player currentPlayer = game.getCurrentPlayer();
        PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
        mysteryState.executeMystery(game ,currentPlayer, currentPlayerView);
     
        dialog.showAlert("Malus Square!", "You landed on a penalty square. Moving back " + " spaces."); 
        
        // Affiche l'animation gif
        displayGif(MALUS_GIF);
        MenuController.getSecondarySound().playMedia("malus.wav", SOUND_VOLUME);
        
        // Passe au joueur suivant SANS déclencher une autre vérification de case
        nextPlayer();
        
        // Important : utilisez une pause pour attendre la fin de l'animation
        // sans déclencher de nouveau displayQuestionCardBasedOnPosition
        PauseTransition waitTransition = new PauseTransition(Duration.seconds(ANIMATION_DURATION + 0.2));
        waitTransition.setOnFinished(e -> {
            // Ne rien faire ici - juste attendre que l'animation se termine
            // avant de permettre d'autres actions
        });
        waitTransition.play();
    }
    
    private void handleSwitchPlayers() {
    	 mysteryState = new Bonus4();
    	 Player currentPlayer = game.getCurrentPlayer();
 		 PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
 		 mysteryState.executeMystery(game, currentPlayer, currentPlayerView, playerViews);
         nextPlayer();
    	
    }
    
   private void handlePlayerBonusForward() {
	    mysteryState = new Bonus1();
		Player currentPlayer = game.getCurrentPlayer();
		PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
		mysteryState.executeMystery(game, currentPlayer, currentPlayerView);
		dialog.showAlert("Bonus Square!", "You have moved forward 2 spaces.");

		// Affiche l'animation gif
		displayGif(BONUS_GIF);
		MenuController.getSecondarySound().playMedia("bonus.wav", SOUND_VOLUME);

		// Passe au joueur suivant SANS déclencher une autre vérification de case
		nextPlayer();

		// Important : utilisez une pause pour attendre la fin de l'animation
		// sans déclencher de nouveau displayQuestionCardBasedOnPosition
		PauseTransition waitTransition = new PauseTransition(Duration.seconds(ANIMATION_DURATION + 0.2));
		waitTransition.setOnFinished(e -> {
			// Ne rien faire ici - juste attendre que l'animation se termine
			// avant de permettre d'autres actions
		});
		waitTransition.play();
	   
   }
    
    

    /**
     * Displays a question card based on the color of the space.
     * @param fillColor The color of the space
     */
    private void displayQuestionCardByColor(String fillColor) {
        Topic selectedTheme = null;
        
        // Determine theme based on color
        if (fillColor.contains(purpleStr)) {
            selectedTheme = Topic.IMPROBABLE;
        } else if (fillColor.contains(yellowStr)) {
            selectedTheme = Topic.ENTERTAINMENT;
        } else if (fillColor.contains(blueStr)) {
            selectedTheme = Topic.INFORMATICS;
        } else if (fillColor.contains(greenStr)) {
            selectedTheme = Topic.EDUCATION;
        } else {
            // For any other color, select a random theme
            Topic[] allThemes = Topic.values();
            selectedTheme = allThemes[random.nextInt(allThemes.length)];
        }
        
        displayQuestionCardForTheme(selectedTheme);
    }

    /**
     * Displays a question card for the specified theme.
     * @param theme The theme for which to display a question
     */
    private void displayQuestionCardForTheme(Topic theme) {
        // Find cards of the specified theme that haven't been used yet
        List<QuestionCard> availableCards = questionCards.stream()
                .filter(card -> card.getTheme() == theme && !usedQuestionCards.contains(card))
                .collect(Collectors.toList());
        
        if (availableCards.isEmpty()) {
            // If all cards of this theme have been used, reset and select from all theme cards
            availableCards = questionCards.stream()
                    .filter(card -> card.getTheme() == theme)
                    .collect(Collectors.toList());
            
            if (availableCards.isEmpty()) {
                // Fallback if no cards for this theme exist
                dialog.showAlert("No Questions", "No questions available for this theme. Moving to next player.");
                nextPlayer();
                waitForAnimation();
                return;
            }
        }
        
        // Select a random card from available cards
        QuestionCard selectedCard = availableCards.get(random.nextInt(availableCards.size()));
        usedQuestionCards.add(selectedCard);
        
        // Display the selected card
        displayQuestionCard(selectedCard);
    }
    /**
     * Displays the question card with properly formatted content.
     * Adjusts the AnchorPane to fit the content and ensures text is fully visible.
     *
     * @param card The question card to display
     */

private void displayQuestionCard(QuestionCard card) {
	MenuController.getSecondarySound().playMedia("zoom.wav", SOUND_VOLUME);
    themeLabel.setText("Theme: " + card.getTheme().toString());

    List<Question> questions = card.getQuestions();
    Label[] labels = {questionLabel1, questionLabel2, questionLabel3, questionLabel4};

    // Initialize labels
    for (Label label : labels) {
        label.setVisible(false);
        label.setWrapText(true);
        label.setMaxWidth(342.0);
        label.setPrefHeight(USE_COMPUTED_SIZE);
    }

    // Sort questions by priority (1 to 4)
    List<Question> sortedQuestions = questions.stream()
        .sorted((q1, q2) -> q1.getDifficulty() - q2.getDifficulty())
        .collect(Collectors.toList());

    // Display questions in order of priority
    for (int i = 0; i < Math.min(sortedQuestions.size(), labels.length); i++) {
        final int questionIndex = i;
        Question question = sortedQuestions.get(i);
        labels[i].setText(question.getTexte() + " (Priority: " + question.getDifficulty() + ")");
        labels[i].setVisible(true);

        labels[i].setOnMouseClicked(event -> {
        	MenuController.getSecondarySound().playMedia("click2.wav", SOUND_VOLUME);
            displaySelectedQuestion(question);
        });
    }

    questionCard.setVisible(true);
    playTransition(questionCard, false);
    questionsContainer.setVisible(true);
    questionBox.setVisible(false);
    themeLabel.setVisible(true);

    questionCard.setPrefHeight(USE_COMPUTED_SIZE);
    questionsContainer.setPrefHeight(USE_COMPUTED_SIZE);

    SequentialTransition sequentialTransition = new SequentialTransition();
    sequentialTransition.getChildren().add(playTransitionLabel(themeLabel));

    for (Label label : labels) {
        if (label.isVisible()) {
            sequentialTransition.getChildren().add(playTransitionLabel(label));
        }
    }

    sequentialTransition.play();
}

    
    /**
     * Displays the selected question with its possible answers.
     * 
     * @param question The question to display
     */

private void displaySelectedQuestion(Question question) {
    questionsContainer.setVisible(false);
    questionBox.setVisible(true);

    questionSelectionneeLabel.setText(question.getTexte());

    reponse.selectToggle(null);

    // Reset all RadioButtons state (fixes the hint persistence issue)
    RadioButton[] responseButtons = {response1, response2, response3, response4};
    for (RadioButton button : responseButtons) {
        button.setDisable(false);
        button.setStyle("");  // Clear any custom styling
        button.setVisible(false);  // Will be made visible if needed below
    }

    List<String> responses = new ArrayList<>(question.getResponse());
    // Shuffle the responses
    Collections.shuffle(responses);

    // Keep track of the shuffled indices
    Map<String, Integer> responseToIndex = new HashMap<>();
    for (int i = 0; i < question.getResponse().size(); i++) {
        responseToIndex.put(question.getResponse().get(i), i);
    }

    // Set up the response buttons
    for (int i = 0; i < Math.min(responses.size(), responseButtons.length); i++) {
        responseButtons[i].setText(responses.get(i));
        responseButtons[i].setVisible(true);
        // Store the original index as user data
        responseButtons[i].setUserData(responseToIndex.get(responses.get(i)));
    }

    Main.getMainSound().muteMedia();
    validerButton.setUserData(question);
    startTimer();
}

    

    
    /**
     * Starts a countdown timer for answering questions.
     */
    private void startTimer() {
    	volumeImage.setDisable(true);
    	
    	int seconds;
    	
    	Player currentPlayer = game.getCurrentPlayer();
    	if (currentPlayer.getPosition() <= 8) {
    		seconds = 20;
    	}else if(currentPlayer.getPosition() <= 16) {
    		seconds = 15;
    	}else {
    		 seconds = 10;
    	}
    	
        int halfTime = seconds / 2;
        timerLabel.setText(String.valueOf(seconds));
        timerLabel.setVisible(true);
        timerImage.setVisible(true);

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
        timeline.setCycleCount(seconds);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
            int timeLeft = Integer.parseInt(timerLabel.getText());
            timeLeft--;
            timerLabel.setText(String.valueOf(timeLeft));

            if (timeLeft <= halfTime) {
                timerLabel.setStyle("-fx-text-fill: red;");
            }

            if (timeLeft <= 0) {
            	//Player currentPlayer = game.getCurrentPlayer();
            	PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
                stopTimer();
                toggleQuestionCardVisibility();
                currentPlayer.decreaseScore(25);
                currentPlayer.resetStreak();
                int stepsBack = Math.min(1, currentPlayer.getPosition());
                currentPlayer.move(-stepsBack);
                currentPlayerView.animateMovement(-stepsBack);
                updateScoreAndStreakDisplay();
            
                nextPlayer();
            }

            if (timeLeft == seconds - 1) {
            	MenuController.getTimerSound().stopMedia();
            	MenuController.getTimerSound().playMedia("timerMusic.mp3", 0.1);
            	MenuController.getTimerSound().loop();
            }
        }));
        timeline.play();
    }

    /**
     * Stops the countdown timer.
     */
    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            //sound.playMedia("timerEnd.wav", SOUND_VOLUME);
        }
        MenuController.getTimerSound().stopMedia();
        //sound.playMedia("timerEnd.wav", SOUND_VOLUME);
        timerLabel.setStyle("-fx-text-fill: white;");
        timerLabel.setVisible(false);
        timerImage.setVisible(false);
        volumeImage.setDisable(true);
    }
    
    /**
     * Navigates to another view.
     * 
     * @param fxmlPath Path to the FXML file
     * @param event The action event
     */
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
    
	    
	
	private void initializeScoreAndStreak() {
	    playersScores = new ArrayList<>();
	    playersScores.add(scorePlayer1);
	    playersScores.add(scorePlayer2);
	    playersScores.add(scorePlayer3);
	    playersScores.add(scorePlayer4);
	
	    playersStreaks = new ArrayList<>();
	    playersStreaks.add(streakPlayer1);
	    playersStreaks.add(streakPlayer2);
	    playersStreaks.add(streakPlayer3);
	    playersStreaks.add(streakPlayer4);
	
	    for (int i = 0; i < 4; i++) {
	        if (i < players.size()) {
	            playersScores.get(i).setText("Score: 0");
	            playersStreaks.get(i).setText("Streak: 0");
	        } else {
	            playersScores.get(i).setText("");
	            playersStreaks.get(i).setText("");
	        }
	    }
	}
	
	private void updateScoreAndStreakDisplay() {
	    for (int i = 0; i < players.size(); i++) {
	        Player player = players.get(i);
	        playersScores.get(i).setText("Score: " + player.getScore());
	
	        String streakText = "Streak: " + player.getStreak();
	        playersStreaks.get(i).setText(streakText);
	
	        // Apply highlight styling if streak is active
	        if (player.getStreak() > 0) {
	            playersStreaks.get(i).getStyleClass().add("streak-active");
	        } else {
	            playersStreaks.get(i).getStyleClass().remove("streak-active");
	        }
	    }
	}
	
	private void waitForAnimation() {
	    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
	    pause.setOnFinished(event -> {
	        // Code to execute after the delay
	        displayQuestionCardBasedOnPosition();
	    });
	    pause.play();
	}
	


private void displayGif(String file) {
    // Load the image
    bonusMalusImage.setImage(new Image(file));

    // Configure image properties for display
    bonusMalusImage.setPreserveRatio(false);  // Changed to true for better image proportions
    bonusMalusImage.setSmooth(true);
    bonusMalusImage.setCache(true);

    // Make image fit inside container boundaries
    bonusMalusImage.fitWidthProperty().bind(georgeBonusGifPane.widthProperty().multiply(0.9));
    bonusMalusImage.fitHeightProperty().bind(georgeBonusGifPane.heightProperty().multiply(0.9));

    // Center the image in the pane
    AnchorPane.setTopAnchor(bonusMalusImage, 0.0);
    AnchorPane.setRightAnchor(bonusMalusImage, 0.0);
    AnchorPane.setBottomAnchor(bonusMalusImage, 0.0);
    AnchorPane.setLeftAnchor(bonusMalusImage, 0.0);

    // Show the container
    georgeBonusGifPane.setVisible(true);

    // Apply scaling animation
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), georgeBonusGifPane);
    scaleTransition.setFromX(0);
    scaleTransition.setFromY(0);
    scaleTransition.setToX(1);
    scaleTransition.setToY(1);
    scaleTransition.play();

    // Hide after delay
    PauseTransition pause = new PauseTransition(Duration.seconds(3));
    pause.setOnFinished(e -> georgeBonusGifPane.setVisible(false));
    pause.play();
}


	
	
	

	
	public void nextPlayer() {
		
		
	    for (Player player : players) {
	        player.setUsedHintThisRound(false);
	    }
		
		game.nextPlayer();
	    Player nextPlayer = game.getCurrentPlayer();
	    
	 // Skip the turn if the player is blocked
	    if (nextPlayer.isBlocked()) {
	        dialog.showAlert("Turn Skipped", nextPlayer.getName() + " is blocked");
	        nextPlayer.setBlocked(false); // Unblock the player after skipping their turn
	        nextPlayer(); // Move to the next player
	        return;
	    }
	    
	    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
	        Platform.runLater(() -> {
	            //dialog.showAlert("Next Turn", "It's " + nextPlayer.getName() + "'s turn!");
	            waitForAnimation();
	            for (Label name : playersNames) {
	    			if (name.getText().equals(game.getCurrentPlayer().getName())) {
	    				name.setStyle("-fx-text-fill: orange;");
	    			} else {
	    				name.setStyle("-fx-text-fill: black;");
	    			}	
	    		}
	            
	        });
	    }));
	    timeline.play();
	    checkPlayerOverlap();
	    updatePlayerPostions();
	}
		
	private void playAgain() {
		
		
		
	}
	
	private void removePlayerFromGame(int index) {
		game.getPlayers().remove(index);
    	playerViews.remove(index);
	}
	
	private void updatePlayerPostions(){
		int position;
		System.out.println("\nPositon of each player after the turn");
	    for (int i= 0; i< players.size(); i++) {
	        position = pos;
	  		positions.set(i, position);	
			for (int j= 0; j< players.size(); j++) {
				if (i!= j && players.get(i).getPosition() < players.get(j).getPosition()) {
					position++;
					positions.set(i,position);		
				}
			}
			System.out.println(players.get(i).getName()+" : "+players.get(i).getPosition());
			
			for (int k= 0; k< players.size(); k++) {
				playersPos.get(k).setText(positions.get(k)+"th");
			}
			
		}
	    System.out.println("Positions des joueurs"+ positions);
	    System.out.println("\n");
	}

	    
	
	    private void quitGame() {
	        players.clear();
	        playersHints.clear();
	        playersNames.clear();
	        PlayerChoiceViewController.getSelectedColors().clear();
	        MenuController.getTimerSound().stopMedia();
	        nbPlayers = 0;
	    }
	    

	    /**
	     * Checks if any two players are on overlapping spaces.
	     * If overlapping players are found, the player with the higher score moves forward
	     * and the player with the lower score moves back 2 spaces.
	     * No questions are asked after movement, and the game proceeds to the next player.
	     */
	    private void checkPlayerOverlap() {
	        // Define the common spaces where lanes intersect
	        Map<String, String> commonSpaces = new HashMap<>();
	        commonSpaces.put("rec1_7", "rec3_10");  // Lane 1 and Lane 3 intersection
	        commonSpaces.put("rec2_6", "rec3_7");   // Lane 2 and Lane 3 intersection
	        commonSpaces.put("rec1_10", "rec2_10"); // Lane 1 and Lane 2 intersection
	        commonSpaces.put("rec1_13", "rec4_13"); // Lane 1 and Lane 4 intersection
	        commonSpaces.put("rec2_21", "rec4_21"); // Lane 2 and Lane 4 intersection

	        boolean overlapDetected = false;

	        // Check each player against other players
	        for (int i = 0; i < players.size(); i++) {
	            Player player1 = players.get(i);
	            int position1 = player1.getPosition();
	            PlayerView playerView1 = playerViews.get(i);

	            // Get the rectangle ID where player1 is located
	            String rec1Id = "";
	            if (position1 < playerView1.getSpaces().size()) {
	                rec1Id = playerView1.getSpaces().get(position1).getId();
	            }

	            for (int j = i + 1; j < players.size(); j++) {
	                Player player2 = players.get(j);
	                int position2 = player2.getPosition();
	                PlayerView playerView2 = playerViews.get(j);

	                // Get the rectangle ID where player2 is located
	                String rec2Id = "";
	                if (position2 < playerView2.getSpaces().size()) {
	                    rec2Id = playerView2.getSpaces().get(position2).getId();
	                }

	                // Skip if either rectangle ID is empty
	                if (rec1Id.isEmpty() || rec2Id.isEmpty()) continue;

	                boolean isOverlapping = false;

	                // Check if players are on the same space directly
	                if (rec1Id.equals(rec2Id)) {
	                    isOverlapping = true;
	                }
	                
	                // Check if players are on intersecting spaces
	                if (!isOverlapping) {
	                    if (commonSpaces.containsKey(rec1Id) && commonSpaces.get(rec1Id).equals(rec2Id)) {
	                        isOverlapping = true;
	                    } else if (commonSpaces.containsKey(rec2Id) && commonSpaces.get(rec2Id).equals(rec1Id)) {
	                        isOverlapping = true;
	                    }
	                }

	                // Handle overlap if detected
	                if (isOverlapping) {
	                    overlapDetected = true;
	                    handlePlayerCollision(player1, player2, i, j);
	                }
	            }
	        }
	    }

	    /**
	     * Handles collision between two players.
	     * The player with the higher score moves forward 2 spaces,
	     * and the player with the lower score moves back 2 spaces.
	     * 
	     * @param player1 First player
	     * @param player2 Second player
	     * @param index1 Index of first player
	     * @param index2 Index of second player
	     */
	    private void handlePlayerCollision(Player player1, Player player2, int index1, int index2) {
	        Player higherScorePlayer;
	        Player lowerScorePlayer;
	        int higherScoreIndex;
	        int lowerScoreIndex;
	        
	        // First message - Battle announcement
	        dialog.showAlert("Player Battle!", 
	            "A battle is taking place between " + player1.getName() + " and " + player2.getName() + "!");
	        
	        // Determine which player has the higher score
	        if (player1.getScore() >= player2.getScore()) {
	            higherScorePlayer = player1;
	            lowerScorePlayer = player2;
	            higherScoreIndex = index1;
	            lowerScoreIndex = index2;
	        } else {
	            higherScorePlayer = player2;
	            lowerScorePlayer = player1;
	            higherScoreIndex = index2;
	            lowerScoreIndex = index1;
	        }
	        
	        // Create sequence of actions with delays
	        PauseTransition battlePause = new PauseTransition(Duration.seconds(2));
	        battlePause.setOnFinished(e -> {
	            // Second message - Winner announcement
	            dialog.showAlert("Battle Result", 
	                higherScorePlayer.getName() + " wins with a score of " + higherScorePlayer.getScore() + 
	                " against " + lowerScorePlayer.getName() + " with a score of " + lowerScorePlayer.getScore() + "!");
	            
	            PauseTransition resultPause = new PauseTransition(Duration.seconds(2));
	            resultPause.setOnFinished(ev -> {
	                // Third message - Movement announcement
	                dialog.showAlert("Player Movement", 
	                    higherScorePlayer.getName() + " moves forward 2 spaces and " +
	                    lowerScorePlayer.getName() + " moves back 2 spaces.");
	                
	                PauseTransition movePause = new PauseTransition(Duration.seconds(1));
	                movePause.setOnFinished(evt -> {
	                    // Move the players
	                    PlayerView higherScoreView = playerViews.get(higherScoreIndex);
	                    PlayerView lowerScoreView = playerViews.get(lowerScoreIndex);
	                    
	                    // Check if higher score player can move forward (not at the end)
	                    int highPlayerMaxPos = higherScoreView.getSpaces().size() - 1;
	                    int moveForwardSteps = Math.min(2, highPlayerMaxPos - higherScorePlayer.getPosition());
	                    
	                    // Move higher score player forward
	                    higherScorePlayer.move(moveForwardSteps);
	                    higherScoreView.animateMovement(moveForwardSteps);
	                    
	                    // Move lower score player back (but not before start)
	                    int moveBackSteps = Math.min(2, lowerScorePlayer.getPosition());
	                    lowerScorePlayer.move(-moveBackSteps);
	                    lowerScoreView.animateMovement(-moveBackSteps);
	                    
	                    // Optional: Display animation for collisions
	                    // displayGif("collision.gif");
	                    
	                    // Optional: Play sound effect
	                    // MenuController.getSecondarySound().playMedia("battle.wav", SOUND_VOLUME);
	                    
	                    // Update player positions display
	                    updatePlayerPostions();
	                });
	                movePause.play();
	            });
	            resultPause.play();
	        });
	        battlePause.play();
	        
	    }
}




