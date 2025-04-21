package models;
/**
 * Represents a player in the game. Each player has attributes such as name,
 * position, score, hints, and other game-related states.
 */
public class Player {

	private String name; // Name of the player
	private int position = 0; // Player's position on the board
	private int score = 0; // Player's score
	private int hint = 3; // Number of hints left, default is 3
	private int hintCount = 0; // Number of hints used in the current round
	private boolean isAtTheEnd = false; // Indicates if the player has reached the end of the game
	private boolean usedHintThisRound; // Indicates if the player used a hint in the current round
	private boolean isBlocked = false; // Indicates if the player is blocked
	private int streak = 0; // Number of consecutive correct answers
	private int nbQuestionsAnswerd = 0; // Number of questions answered by the player

	// --- Constructor ---
	/**
	 * Creates a new player with the specified name.
	 * 
	 * @param name The name of the player.
	 */
	public Player(String name) {
		setName(name);
		this.usedHintThisRound = false;
	}

	// --- Movement Methods ---
	/**
	 * Moves the player by the specified number of steps. Ensures the position does
	 * not go below 0.
	 * 
	 * @param steps The number of steps to move.
	 */
	public void move(int steps) {
		position += steps;
		position = Math.max(position, 0); // Ensure position is not negative
	}

	/**
	 * Sets the player's position.
	 * 
	 * @param position The new position.
	 * @return The updated position.
	 */
	public int setPosition(int position) {
		return this.position = position;
	}
	
		public int getPosition() {
			return position;
		}

	// --- Score Management Methods ---
	/**
	 * Increases the player's score by the specified amount.
	 * 
	 * @param score The amount to increase.
	 */
	public void increaseScore(int score) {
		if (score < 0) {
			throw new IllegalArgumentException("Score to increase cannot be negative.");
		}
		this.score += score;
	}

	/**
	 * Decreases the player's score by the specified amount. Ensures the score does
	 * not go below 0.
	 * 
	 * @param score The amount to decrease.
	 */
	public void decreaseScore(int score) {
		if (score < 0) {
			throw new IllegalArgumentException("Score to decrease cannot be negative.");
		}
		this.score = Math.max(this.score - score, 0);
	}

	/**
	 * Sets the player's score and increments the number of questions answered.
	 * 
	 * @param score The new score.
	 */
	public void setScore(int score) {
		if (score < 0) {
			throw new IllegalArgumentException("Score cannot be negative.");
		}
		this.score += score;
		this.nbQuestionsAnswerd++;
	}
	
	public int getScore() {
		return score;
	}

	/**
	 * Calculates the player's average score.
	 * 
	 * @return The average score, or 0 if no questions have been answered.
	 */
	public int averageScore() {
		return nbQuestionsAnswerd == 0 ? 0 : score / nbQuestionsAnswerd;
	}

	// --- Hint Management Methods ---
	/**
	 * Sets the number of hints available to the player. Ensures the total does not
	 * exceed 3.
	 * 
	 * @param hint The number of hints to set.
	 */
	public void setHint(int hint) {
		if (hint < 0) {
			throw new IllegalArgumentException("Hint cannot be negative.");
		}
		this.hint = Math.min(this.hint + hint, 3);
	}

	/**
	 * Decreases the number of hints by 1. Throws an exception if no hints are left.
	 */
	public void useHint() {
		if (hint <= 0) {
			throw new IllegalStateException("No hints left to use.");
		}
		hint--;
	}
	
	/**
	 * Retrieves the number of hints available to the player.
	 * 
	 * @return The number of hints.
	 */
	public int getHint() {
		return hint;
	}
	
	/**
	 * Checks if the player has any hints left.
	 * 
	 * @return True if hints are available, false otherwise.
	 */
	public boolean hasUsedHintThisRound() {
		return usedHintThisRound;
	}

	/**
	 * Sets the hint usage status for the current round.
	 * 
	 * @param usedHintThisRound True if a hint was used, false otherwise.
	 */
	public void setUsedHintThisRound(boolean usedHintThisRound) {
		this.usedHintThisRound = usedHintThisRound;
	}

	/**
	 * Increases the hint count for the current round.
	 */
	public void increasehintCount() {
		hintCount++;
	}

	/**
	 * Sets the hint count for the player.
	 * 
	 * @param hintCount The new hint count.
	 */
	public void setHintCount(int hintCount) {
		this.hintCount = hintCount;
	}

	/**
	 * Retrieves the number of hints used in the current round.
	 * 
	 * @return The hint count.
	 */
	public int getHintCount() {
		return hintCount;
	}

	// --- End of Game Methods ---
	/**
	 * Marks the player as having reached the end of the game if their position is
	 * 23 or higher.
	 */
	public void setAtTheEnd() {
		isAtTheEnd = position >= 23;
	}

	/**
	 * Checks if the player has reached the end of the game.
	 * 
	 * @return True if the player is at the end, false otherwise.
	 */
	public boolean isAtTheEnd() {
		return isAtTheEnd;
	}

	// --- Streak Management Methods ---
	/**
	 * Retrieves the player's current streak.
	 * 
	 * @return The streak count.
	 */
	public int getStreak() {
		return streak;
	}

	/**
	 * Increases the player's streak by 1.
	 */
	public void increaseStreak() {
		streak++;
	}

	/**
	 * Resets the player's streak to 0.
	 */
	public void resetStreak() {
		streak = 0;
	}

	/**
	 * Checks if the player has a streak of 3 or more.
	 * 
	 * @return True if the streak is 3 or more, false otherwise.
	 */
	public boolean hasThreeStreaks() {
		return streak >= 3;
	}

	// --- Block Management Methods ---
	/**
	 * Checks if the player is blocked.
	 * 
	 * @return True if the player is blocked, false otherwise.
	 */
	public boolean isBlocked() {
		return isBlocked;
	}

	/**
	 * Sets the player's blocked status.
	 * 
	 * @param isBlocked True to block the player, false to unblock.
	 */
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	// --- Question Management Methods ---
	/**
	 * Increments the number of questions answered by the player.
	 */
	public void incrementQuestionsAnswered() {
		nbQuestionsAnswerd++;
	}

	// --- Name Management Methods ---
	/**
	 * Retrieves the player's name.
	 * 
	 * @return The player's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the player's name.
	 * 
	 * @param name The new name.
	 */
	public void setName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Invalid name. Name cannot be null or empty.");
		}
		this.name = name;
	}

}
