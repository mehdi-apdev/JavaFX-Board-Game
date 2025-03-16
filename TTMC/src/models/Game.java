package models;

import java.util.List;

public class Game {
	
	//List of players
	private List<Player> players; //max 4 players
	//private Board board;
	private int currentPlayerIndex;
	//private QuestionFactory questionFactory;
	//private Question currentQuestion;
	//private boolean isFinished;
	
	//Constructor
	/*public Game(List<Player> players, Board board, QuestionFactory questionFactory) {
		this.players = players;
		this.board = board;
		this.questionFactory = questionFactory;
		this.currentPlayerIndex = 0;
		this.isFinished = false;
	}
	*/
	
	public Game(List<Player> players) {
		this.players = players;
		this.currentPlayerIndex = 0;
	}
	
	//Method to start the game
	public void start() {
		// TODO
	}
	
	
	//Method to go to the next player
	public void nextPlayer() {
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}
	
	//Method to get the current player
	public Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
	
	//Method to get the list of players
	public List<Player> getPlayers() {
		return players;
	}
	
	//method to get the current player index
	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}
	
	
	
	
	

}
