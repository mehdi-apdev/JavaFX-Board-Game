package models;
import view.PlayerView;
import java.util.List;
/**
 * The MalusSteps class implements the MysteryState interface. It represents a
 * mystery that applies a penalty to the player's movement by moving them
 * backward.
 */
public class MalusSteps implements MysteryState {

	/**
	 * Executes the mystery action for the current player. This method moves the
	 * current player backward by a fixed number of steps and animates the movement
	 * in the player's view.
	 */
	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView) {
		int steps = 2; // Fixed number of steps to move backward
		currentPlayer.move(-steps); // Move the player backward
		currentPlayerView.animateMovement(-steps); // Animate the movement in the player's view
	}

	/**
	 * Executes the mystery action for the current player and all players. This
	 * method is not implemented in this class.
	 */
	@Override
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> player) {
		// Method not implemented
	}
}
