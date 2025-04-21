
package models;

import java.util.List;
import exceptions.NoPlayersInGameException;

public class Game {

	// List of players in the game (maximum 4 players)
	private List<Player> players;

	// Index of the current player
	private int currentPlayerIndex;
	//private Board board;
	//private QuestionFactory questionFactory;
	//private Question currentQuestion;
	//private boolean isFinished;

	// Constructor
	public Game(List<Player> players) {
		this.players = players;
		this.currentPlayerIndex = 0;
	}
	//List of players
	
	//Constructor
	/*public Game(List<Player> players, Board board, QuestionFactory questionFactory) {
		this.players = players;
		this.board = board;
		this.questionFactory = questionFactory;
		this.currentPlayerIndex = 0;
		this.isFinished = false;
	}*/

	// --- Game Management Methods ---

	/**
	 * Starts the game. (Currently not implemented, placeholder for future logic)
	 */
	public void start() {
		// TODO: Add logic to initialize and start the game
	}

	/**
	 * Ends the current player's turn and moves to the next player. Wraps around to
	 * the first player if the end of the list is reached.
	 */
	public void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	/**
	 * Retrieves the next player in the game and updates the current player index.
	 * 
	 * @return The next player.
	 * @throws NoPlayersInGameException if there are no players in the game.
	 */
	public Player getNextPlayer() {
		if (players.isEmpty()) {
			throw new NoPlayersInGameException("No players available in the game.");
		}
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		return players.get(currentPlayerIndex);
	}

	
	// --- Player Access Methods ---
	/**
	 * Retrieves the current player.
	 * 
	 * @return The current player.
	 * @throws NoPlayersInGameException if there are no players in the game.
	 */
	public Player getCurrentPlayer() {
		if (players.isEmpty()) {
			throw new NoPlayersInGameException("No players available in the game.");
		}
		return players.get(currentPlayerIndex);
	}

	/**
	 * Retrieves the list of all players in the game.
	 * 
	 * @return The list of players.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Retrieves the index of the current player.
	 * 
	 * @return The index of the current player.
	 * @throws NoPlayersInGameException if there are no players in the game.
	 */
	public int getCurrentPlayerIndex() {
		if (players.isEmpty()) {
			throw new NoPlayersInGameException("No players available in the game.");
		}
		return currentPlayerIndex;
	}

	/**
	 * Sets the current player index to a specific value.
	 * 
	 * @param i The new index for the current player.
	 * @throws IllegalArgumentException if the index is invalid.
	 */
	public void setCurrentPlayerIndex(int i) {
		if (i < 0 || i >= players.size()) {
			throw new IllegalArgumentException("Invalid player index");
		}
		this.currentPlayerIndex = i;
	}
}
