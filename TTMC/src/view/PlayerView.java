
package view;

import java.util.List;

import controller.Sound;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.Player;

public class PlayerView {

    private Player player;
    private Circle circle; // visual representation of the player
    private List<Rectangle> spaces; // visual representation of the spaces
    private Sound sound = new Sound("ressources/sounds"); 
    // Constructor
    public PlayerView(Player player, Color color, List<Rectangle> spaces) {
        this.player = player;
        this.spaces = spaces;

        // Create the circle
        this.circle = new Circle(12, color);


        // Set the initial position of the circle
        updatePosition();
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
            circle.setCenterX(rect.getLayoutX() + rect.getWidth() / 2);
            circle.setCenterY(rect.getLayoutY() + rect.getHeight() / 2);
        }
    }

    // Method to animate the player
    public void animate() {
    	sound.playMedia(1);
    }
}
