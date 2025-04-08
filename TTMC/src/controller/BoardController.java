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
import java.util.stream.Collectors;

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
import models.DialogWindow;
import models.EducationQuestionFactory;
import models.EntertainmentQuestionFactory;
import models.Game;
import models.ImprobableQuestionFactory;
import models.InformaticsQuestionFactory;
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
    private static List<Label> playersNames;
    private static List<Label> playersHints;
    private Timeline timeline; 
    private static Game game;
    private PlayerView playerView;
    private List<QuestionCard> questionCards;
    private int currentCardIndex = 0;
    private Random random = new Random();
    
    //Windows for aletts
    private DialogWindow dialog = new DialogWindow();
    
    @FXML private Button btnBack, validerButton;
    @FXML private Pane board, playersContainer, georgeBonusGifPane;
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
	@FXML private Label streakPlayer1, streakPlayer2, streakPlayer3, streakPlayer4;
	

	private static final String purpleStr = "611a44";
	private static final String yellowStr= "c19632";
	private static final String blueStr = "0084ff";
	private static final String greenStr = "7aa823";
	private static final String redStr = "0x8a1515ff";
	private static final String whiteStr = "ffffff";
	private static boolean isStreak;
	
	private List<Label> playersScores;
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
        /*List<Circle> allCircles = new ArrayList<>();
        for (Node node : board.getChildren()) {
            if (node instanceof Circle) {
                allCircles.add((Circle) node);
                System.out.println(node);
            }
        }*/
        
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
                hint.setText(players.get(i-1).getHint()+" left(s)");
            } else {
                hint.setText("/");
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
                playerName.setText("/");
            }
            i++;
        }
        
        return nbPlayers;
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
    	MenuController.getSecondarySound().playMedia("hint.wav", SOUND_VOLUME);
        currentPlayer.useHint();
        currentPlayer.setUsedHintThisRound(true);
        dialog.showAlert("Hint used", "You have " + currentPlayer.getHint() + " hint(s) left.");
        // Logic to provide a hint to the player
    } else {
        dialog.showAlert("No hints left", "You have no hints left or have already used a hint this round.");
    }
    updateHintsDisplay();
}

private void updateHintsDisplay() {
    for (int i = 0; i < players.size(); i++) {
        playersHints.get(i).setText(players.get(i).getHint() + " left(s)");
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
        boolean isCorrect = currentQuestion.isCorrectResponse(selectedResponseIndex);
        Player currentPlayer = game.getCurrentPlayer();

        if (isCorrect) {
        	stopTimer();
        	MenuController.getSecondarySound().playMedia("good.wav",SOUND_VOLUME);
            currentPlayer.increaseScore();
            currentPlayer.increaseStreak();
            int stepsToMove = currentQuestion.getDifficulty();
           

            
            dialog.showAlert("Correct answer!", "You move forward " + stepsToMove + " space(s).");
            
            
            
            if (currentPlayer.hasThreeStreaks()) {
              
                        dialog.showAlert("Three in a row!", "You have answered 3 questions correctly in a row!");
                        currentPlayer.resetStreak();
                        stepsToMove += 2;
                        displayGif(BONUS_GIF);
                        MenuController.getSecondarySound().playMedia("bonus.mp3", SOUND_VOLUME);
            }
            
           
    		movePlayerForward(stepsToMove);
            
        } else {
        	stopTimer();
        	MenuController.getSecondarySound().playMedia("wrong.mp3",SOUND_VOLUME);
            currentPlayer.resetStreak();
            dialog.showAlert("Wrong answer!", "Better luck next time!");
            stopTimer();
        }
        stopTimer();
        updateScoreAndStreakDisplay();
        toggleQuestionCardVisibility();

        // Get next player before showing the turn message
        game.nextPlayer();
        Player nextPlayer = game.getCurrentPlayer();
       // playerViews.

        // Show turn message and question with a slight delay

		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
			Platform.runLater(() -> {
				dialog.showAlert("Next Turn", "It's " + nextPlayer.getName() + "'s turn!");
				waitForAnimation();
				
			});
		}));
		timeline.play();
		        // Reset the timer
		        stopTimer();
		       
                
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
    PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
    pause.setOnFinished(e -> {
        Platform.runLater(() -> {
            Player currentPlayer = game.getCurrentPlayer();
            PlayerView currentPlayerView = playerViews.get(game.getCurrentPlayerIndex());
            int position = currentPlayer.getPosition();

            // Debugging
            System.out.println("Current Player: " + currentPlayer.getName());
            System.out.println("Current Position: " + position);

            if (position >= currentPlayerView.getSpaces().size()) {
                return;
            }

            Rectangle currentRectangle = currentPlayerView.getSpaces().get(position);
            String fillColor = currentRectangle.getFill().toString();

            // Debugging
            System.out.println("Rectangle color at position " + position + ": " + fillColor);

            // Handle red rectangle (malus case)
            if (fillColor.equalsIgnoreCase(redStr) || currentRectangle.getStyleClass().contains("malus")) {
                System.out.println("RED " + game.getCurrentPlayer().getName());
                int stepsBack = Math.min(2, position);
                currentPlayer.move(-stepsBack);
                currentPlayerView.animateMovement(-stepsBack);

                dialog.showAlert("Malus Square!", "You landed on a penalty square. Moving back 2 spaces.");
                displayGif(MALUS_GIF);
                MenuController.getSecondarySound().playMedia("malus.wav", 0.1);
                game.nextPlayer();

                Player nextPlayer = game.getCurrentPlayer();
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), evt -> {
                    Platform.runLater(() -> {
                        dialog.showAlert("Next Turn", "It's " + nextPlayer.getName() + "'s turn!");
                        waitForAnimation();


                    });
                }));
                timeline.play();
                return;
            }

            Topic theme;

            // Determine theme based on rectangle color
            if (fillColor.equalsIgnoreCase(whiteStr)) {
                Topic[] allThemes = Topic.values();
                theme = allThemes[random.nextInt(allThemes.length)];
                System.out.println("White " + game.getCurrentPlayer().getName());
            } else if (fillColor.contains(purpleStr)) {
                theme = Topic.IMPROBABLE;
                System.out.println("Mauve " + game.getCurrentPlayer().getName());
            } else if (fillColor.contains(yellowStr)) {
                theme = Topic.ENTERTAINMENT;
                System.out.println("Jaune " + game.getCurrentPlayer().getName());
            } else if (fillColor.contains(blueStr)) {
                theme = Topic.INFORMATICS;
                System.out.println("Bleu " + game.getCurrentPlayer().getName());
            } else if (fillColor.contains(greenStr)) {
                theme = Topic.EDUCATION;
                System.out.println("Vert " + game.getCurrentPlayer().getName());
            } else {
                Topic[] allThemes = Topic.values();
                theme = allThemes[random.nextInt(allThemes.length)];
                System.out.println("Random " + game.getCurrentPlayer().getName());
            }

            QuestionCard selectedCard = null;
            List<QuestionCard> availableCards = questionCards.stream()
                    .filter(card -> card.getTheme() == theme && !usedQuestionCards.contains(card))
                    .collect(Collectors.toList());

            if (!availableCards.isEmpty()) {
                selectedCard = availableCards.get(random.nextInt(availableCards.size()));
                usedQuestionCards.add(selectedCard);
            } else {
                // Reset used questions if all questions of the theme have been used
                usedQuestionCards.clear();
                availableCards = questionCards.stream()
                        .filter(card -> card.getTheme() == theme)
                        .collect(Collectors.toList());
                if (!availableCards.isEmpty()) {
                    selectedCard = availableCards.get(random.nextInt(availableCards.size()));
                    usedQuestionCards.add(selectedCard);
                }
            }

            if (selectedCard != null) {
                displayQuestionCard(selectedCard);
            }
        });
    });
    pause.play();
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

        List<String> responses = new ArrayList<>(question.getResponse());
        // Shuffle the responses
        Collections.shuffle(responses);

        // Keep track of the shuffled indices
        Map<String, Integer> responseToIndex = new HashMap<>();
        for (int i = 0; i < question.getResponse().size(); i++) {
            responseToIndex.put(question.getResponse().get(i), i);
        }

        RadioButton[] responseButtons = {response1, response2, response3, response4};
        for (int i = 0; i < Math.min(responses.size(), responseButtons.length); i++) {
            responseButtons[i].setText(responses.get(i));
            responseButtons[i].setVisible(true);
            // Store the original index as user data
            responseButtons[i].setUserData(responseToIndex.get(responses.get(i)));
        }

        for (int i = responses.size(); i < responseButtons.length; i++) {
            responseButtons[i].setVisible(false);
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
        int seconds = 20;
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

            if (timeLeft <= 10) {
                timerLabel.setStyle("-fx-text-fill: red;");
            }

            if (timeLeft <= 0) {
                stopTimer();
                questionCard.setVisible(false);
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
	 bonusMalusImage.setImage(new Image(file));
	 bonusMalusImage.setPreserveRatio(false);
	 bonusMalusImage.setFitWidth(650); // Set desired width
	 bonusMalusImage.setFitHeight(365); // Set desired height
     georgeBonusGifPane.setVisible(true);
     ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1), georgeBonusGifPane);
     scaleTransition.setFromX(0);
     scaleTransition.setFromY(0);
     scaleTransition.setToX(1);
     scaleTransition.setToY(1);
     scaleTransition.play();
     
     
     //create transition
     PauseTransition pause = new PauseTransition(Duration.seconds(3));
     pause.setOnFinished(e -> {
         
     	georgeBonusGifPane.setVisible(false);
     });
     pause.play();  
}

    

    private void quitGame() {
        players.clear();
        playersHints.clear();
        playersNames.clear();
        PlayerChoiceViewController.getSelectedColors().clear();
        MenuController.getTimerSound().stopMedia();
        nbPlayers = 0;
    }
}

