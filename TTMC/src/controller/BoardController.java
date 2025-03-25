package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import models.Topic;
import view.PlayerView;
import javafx.scene.paint.Color;

/**
 * Controller class for the game board interface.
 * Manages player movement, question cards display, and game interactions.
 */
public class BoardController {
    private static final String VOLUME_ON_IMAGE = "file:ressources/images/maxVolume.png";
    private static final String VOLUME_OFF_IMAGE = "file:ressources/images/noVolume.png";
    private static final int MAX_DICE_VALUE = 4;
    private static final int MIN_DICE_VALUE = 1;
    private static final double ANIMATION_DURATION = 0.6;
    private static final String CLICK_SOUND = "click2.wav";
    private static final double SOUND_VOLUME = 0.5;
    private static final double USE_COMPUTED_SIZE = -1;
    
    
    private static int nbPlayers = 0;
    private static List<Player> players;
    private static List<Label> playersNames;
    private static List<Label> playersHints;
    private Timeline timeline; 
    private final Sound touchSound = new Sound();
    private final Sound timerSound = new Sound();
    private Game game;
    private PlayerView playerView;
    private Sound sound = new Sound();
    private List<QuestionCard> questionCards;
    private int currentCardIndex = 0;
    private Random random = new Random();
    
    @FXML private Button btnBack, validerButton;
    @FXML private Pane board, playersContainer;
    @FXML private CheckBox musicCheckBox;
    @FXML private AnchorPane questionCard;
    @FXML private ImageView volumeImage;
    @FXML private VBox questionsContainer, questionBox;
    @FXML private Label themeLabel, questionLabel1, questionLabel2, questionLabel3, questionLabel4;
    @FXML private Label namePlayer1, namePlayer2, namePlayer3, namePlayer4, timerLabel;
    @FXML private Label playerHint1, playerHint2, playerHint3, playerHint4;
    @FXML private RadioButton response1, response2, response3, response4;
    @FXML private Label questionSelectionneeLabel;
    @FXML private ToggleGroup reponse;
    @FXML private ImageView timerImage;
    
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
        
        List<Rectangle> selectedSpaces = allSpacesList.get(random.nextInt(allSpacesList.size()));
        
        javafx.scene.paint.Paint playerColor = PlayerChoiceViewController.getSelectedColor(); 
        
        playerView = new PlayerView(game.getCurrentPlayer(), 
                playerColor != null ? playerColor : javafx.scene.paint.Color.RED, 
                selectedSpaces);
        playerView.updatePosition();
        
        
       // board.getChildren().add(questionCard);
        board.getChildren().add(playerView.getCircle());
    }
    
    private void initializeRectangleColors(List<Rectangle> allSpaces) {
        Color[] colors = {Color.web("#611a44"), Color.web("#c19632"), Color.web("#0084ff"), Color.web("#7aa823")};
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
        boolean isMuted = Main.mainSound.isMuted();
        musicCheckBox.setSelected(!isMuted);
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
    private void initializeEventHandlers() {
        board.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        Platform.runLater(this::displayQuestionCardBasedOnPosition);
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
     * Returns to the main menu after confirmation.
     * 
     * @param event The action event
     */
    @FXML
    protected void onButtonClicked(ActionEvent event) {
        touchSound.playMedia(CLICK_SOUND, SOUND_VOLUME);
        boolean result = showConfirmationDialog("YOUR PROGRESS WILL BE LOST", "Are you sure you want to leave the game?");
        if (result) {
            navigateToView("../view/menuView.fxml", event);
            quitGame();
        }
    }
    
    /**
     * Handles keyboard input events.
     * 
     * @param event The key event
     */
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            toggleQuestionCardVisibility();
        }  else if (event.getCode() == KeyCode.A) {
            movePlayerRandomSteps();
        }
    }
    
    /**
     * Moves the player a random number of steps.
     */
    private void movePlayerRandomSteps() {
        int steps = random.nextInt(MAX_DICE_VALUE) + MIN_DICE_VALUE;
        int currentPosition = game.getCurrentPlayer().getPosition();
        int remainingSteps = playerView.getSpaces().size() - currentPosition - 1;

        if (steps > remainingSteps) {
            steps = remainingSteps;
        }

        game.getCurrentPlayer().move(steps);
        playerView.updatePosition();
        playerView.animate();

        displayQuestionCardBasedOnPosition();
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
        sound.playMedia(CLICK_SOUND, SOUND_VOLUME);

        Question currentQuestion = (Question) validerButton.getUserData();

        if (reponse.getSelectedToggle() == null) {
            showAlert("No answer selected", "Please select an answer.");
            return;
        }

        int selectedResponseIndex = (int) reponse.getSelectedToggle().getUserData();
        boolean isCorrect = currentQuestion.isCorrectResponse(selectedResponseIndex);

        if (isCorrect) {
            int stepsToMove = currentQuestion.getDifficulty();
            movePlayerForward(stepsToMove);
            showAlert("Correct answer!", "You move forward " + stepsToMove + " space(s).");
        } else {
            showAlert("Incorrect answer", "You stay at your current position.");
        }

        questionCard.setVisible(false);
        stopTimer();
    }
    
    /**
     * Moves the player forward a specified number of steps.
     * 
     * @param steps Number of steps to move forward
     */
    private void movePlayerForward(int steps) {
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
     * Displays an alert dialog with the specified title and message.
     * 
     * @param title The title of the alert
     * @param message The message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Shows a confirmation dialog with the specified title and text.
     * 
     * @param title The title of the dialog
     * @param text The message text
     * @return true if the user confirmed, false otherwise
     */
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
    
    
    /**
     * Displays a question card based on the player's current position.
     * The theme is determined by the color of the rectangle where the player is located.
     */
    private void displayQuestionCardBasedOnPosition() {
        int position = game.getCurrentPlayer().getPosition();
        Rectangle currentRectangle = playerView.getSpaces().get(position);

        String fillColor = currentRectangle.getFill().toString();
        Topic theme;

        if (fillColor.contains("0x611a44") || fillColor.contains("purple") || fillColor.contains("mauve")) {
            theme = Topic.IMPROBABLE;
        } else if (fillColor.contains("0xc19632") || fillColor.contains("orange")) {
            theme = Topic.ENTERTAINMENT;
        } else if (fillColor.contains("0x0084ff") || fillColor.contains("blue") || fillColor.contains("DODGERBLUE")) {
            theme = Topic.INFORMATICS;
        } else if (fillColor.contains("0x7aa823") || fillColor.contains("green")) {
            theme = Topic.EDUCATION;
        } else {
            theme = Topic.GENERAL;
        }

        QuestionCard selectedCard = null;
        for (QuestionCard card : questionCards) {
            if (card.getTheme() == theme) {
                selectedCard = card;
                break;
            }
        }

        if (selectedCard == null && !questionCards.isEmpty()) {
            selectedCard = questionCards.get(random.nextInt(questionCards.size()));
        }

        if (selectedCard != null) {
            displayQuestionCard(selectedCard);
        }
    }

    /**
     * Displays the question card with properly formatted content.
     * Adjusts the AnchorPane to fit the content and ensures text is fully visible.
     *
     * @param card The question card to display
     */
    private void displayQuestionCard(QuestionCard card) {
        themeLabel.setText("Theme: " + card.getTheme().toString());

        List<Question> questions = card.getQuestions();
        Label[] labels = {questionLabel1, questionLabel2, questionLabel3, questionLabel4};

        for (Label label : labels) {
            label.setVisible(false);
            label.setWrapText(true);
            label.setMaxWidth(342.0);
            label.setPrefHeight(USE_COMPUTED_SIZE);
        }

        for (int i = 0; i < Math.min(questions.size(), labels.length); i++) {
            final int questionIndex = i;
            labels[i].setText(questions.get(i).getTexte());
            labels[i].setVisible(true);

            labels[i].setOnMouseClicked(event -> {
                sound.playMedia("click2.wav", 0.5);
                displaySelectedQuestion(questions.get(questionIndex));
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
    
        List<String> responses = question.getResponse();
    
        RadioButton[] responseButtons = {response1, response2, response3, response4};
        for (int i = 0; i < Math.min(responses.size(), responseButtons.length); i++) {
            responseButtons[i].setText(responses.get(i));
            responseButtons[i].setVisible(true);
            responseButtons[i].setUserData(i);
        }
    
        for (int i = responses.size(); i < responseButtons.length; i++) {
            responseButtons[i].setVisible(false);
        }
    
        validerButton.setUserData(question);
        startTimer();
    }
    

    
    /**
     * Starts a countdown timer for answering questions.
     */
    private void startTimer() {
        int seconds = 60;
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
                timerSound.stopMedia();
                timerSound.playMedia("timerMusic.mp3", 0.3);
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
        }
        timerSound.stopMedia();
        timerLabel.setStyle("-fx-text-fill: white;");
        timerLabel.setVisible(false);
        timerImage.setVisible(false);
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
    
    /**
     * Cleans up resources when quitting the game.
     */
    private void quitGame() {
        players.clear();
        playersHints.clear();
        playersNames.clear();
        timerSound.stopMedia();
        nbPlayers = 0;
    }
}

