
package models;

import view.PlayerView;
import java.util.List;

/**
 * 
 * The MysteryState interface represents the state of a mystery in the game. It
 * defines the behavior of mystery actions that can be executed during the game.
 * Implementations of this interface will provide specific logic for handling
 * mysteries.
 */
public interface MysteryState {
	/**
	 * 
	 * Executes a mystery action for the current player. This method is used when
	 * the mystery only affects the current player.
	 *
	 * @param game              The current game instance, which contains the state
	 *                          of the game.
	 * @param currentPlayer     The player who is currently taking their turn.
	 * @param currentPlayerView The view associated with the current player, used to
	 *                          display updates or interactions.
	 */
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView);
	public void executeMystery(Game game, Player currentPlayer, PlayerView currentPlayerView, List<PlayerView> playerView);
}
