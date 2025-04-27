
package view;

import java.util.List;

import controller.Sound;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Player;

public class PlayerView {

    private Player player;
    private Circle circle; // visual representation of the player
    private List<Rectangle> spaces; // visual representation of the spaces
    private Sound sound = new Sound(); 
    // Constructor
    public PlayerView(Player player, Circle circle , List<Rectangle> spaces) {
        this.player = player;
        this.spaces = spaces;
        this.circle = circle;
       
        // Set the initial position of the circle
        //updatePosition();
    }

    // Getter for circle
    public Circle getCircle() {
        return circle;
    }

    // Update the position of the circle
    public void updatePosition() {
        int position = player.getPosition();

        if (position < spaces.size()) {
            // Get the rectangle in the position of the player
            Rectangle rect = spaces.get(position);

            // Center the circle in the middle of the rectangle
            circle.setLayoutX(rect.getLayoutX() + rect.getWidth() / 2);
            circle.setLayoutY(rect.getLayoutY() + rect.getHeight() / 2);
        }
    }

    // Method to animate the player


    public void animateMovement(int steps) {
        SequentialTransition sequentialTransition = new SequentialTransition();
        int startPosition = player.getPosition() - steps;

        // Create a transition for each step
        for (int i = 0; i < steps; i++) {
            int currentPos = startPosition + i;
            int nextPos = currentPos + 1;

            if (nextPos < spaces.size()) {
                //Rectangle currentRect = spaces.get(currentPos);
                Rectangle nextRect = spaces.get(nextPos);

                // Calculate absolute positions instead of relative translations
                double nextX = nextRect.getLayoutX() + nextRect.getWidth() / 2;
                double nextY = nextRect.getLayoutY() + nextRect.getHeight() / 2;
                //double currentX = currentRect.getLayoutX() + currentRect.getWidth() / 2;
                //double currentY = currentRect.getLayoutY() + currentRect.getHeight() / 2;

                // Create bounce effect
                ParallelTransition stepTransition = new ParallelTransition();

                // Translation movement using absolute positions
                TranslateTransition moveTransition = new TranslateTransition(Duration.seconds(0.4), circle);
                moveTransition.setToX(nextX - circle.getLayoutX());
                moveTransition.setToY(nextY - circle.getLayoutY());

                // Scale animation for bounce effect
                ScaleTransition scaleUp = new ScaleTransition(Duration.seconds(0.2), circle);
                scaleUp.setFromX(1.0);
                scaleUp.setFromY(1.0);
                scaleUp.setToX(1.2);
                scaleUp.setToY(1.2);

                ScaleTransition scaleDown = new ScaleTransition(Duration.seconds(0.2), circle);
                scaleDown.setFromX(1.2);
                scaleDown.setFromY(1.2);
                scaleDown.setToX(1.0);
                scaleDown.setToY(1.0);

                SequentialTransition bounceTransition = new SequentialTransition(scaleUp, scaleDown);

                // Combine movement and bounce
                stepTransition.getChildren().addAll(moveTransition, bounceTransition);
                sequentialTransition.getChildren().add(stepTransition);
            }
        }

        // Reset translation values and update final position at the end of animation
        sequentialTransition.setOnFinished(event -> {
            circle.setTranslateX(0);
            circle.setTranslateY(0);
            updatePosition();
            sound.playMedia("tap.wav", 0.5);
        });

        //sound.playMedia("tap.wav", 0.5);
        sequentialTransition.play();
    }




    
    //Getter of spaces
	public List<Rectangle> getSpaces() {
		return spaces;
	}
}
