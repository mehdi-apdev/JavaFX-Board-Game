package models;

public class Player {
	
	private String name;
	private int position = 0; // Player's position on the board;
	private int score = 0;
	private int hint = 3; // Number of hints left, default 3
    private boolean usedHintThisRound;
    private boolean isBlocked = false; // Indicates if the player is blocked
    private int streak = 0; // Number of consecutive correct answers
    private int nbQuestionsAnswerd = 0; // Number of questions answered by the player
	
	public Player(String name) {
		setName(name);
		 this.usedHintThisRound = false;
	}
	
	// method to move the player
	public void move(int steps) {
		position+= steps;
		position = (position < 0) ? 0 : position; // if the player goes back to the start
	}
	
	// method to increase the score
	public void increaseScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score to increase cannot be negative.");
        }
        this.score += score;
    }
	
	public void decreaseScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score to decrease cannot be negative.");
        }
        
        if (this.score == 0 || this.score < score) {
            this.score = 0;
        } else {
            this.score -= score;
        }
    }
	
	public void incrementQuestionsAnswered() {
	    nbQuestionsAnswerd++;
	}

    public void setScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative.");
        }
        this.score += score;  // Add the score to the current score
        this.nbQuestionsAnswerd++; // Increment the number of questions answered
    }

	public int averageScore() {
	    if (nbQuestionsAnswerd == 0) {
	        return 0;
	    }
	    return score / nbQuestionsAnswerd;
	}
	
	public void setHint(int hint) {
		if (hint < 0) {
			throw new IllegalArgumentException("Hint cannot be negative.");
		}
		
		if (this.hint + hint > 3) {
			hint = 3 - this.hint; // Limit to 3 hints
		}
		
		this.hint += hint;
	}

	
    public void useHint() {
        if (hint <= 0) {
            throw new IllegalStateException("No hints left to use.");
        }
        hint--;
    }
    
	public int getStreak() {
		return streak;
	}

	public void increaseStreak() {
		streak++;
	}
	
	public void resetStreak() {
		streak = 0;
	}
	
	public boolean hasThreeStreaks() {
		return streak >= 3;
	}
	
	// method to get the name of the player
	public String getName() {
		return this.name;
	}
	
	// method to get the position of the player
	public int getPosition() {
		return this.position;
	}
	
	// method to get the score of the player
	public int getScore() {
		return this.score;
	}
	
	// method to get the number of hints left
	public int getHint() {
		return this.hint;
	}
	
	
	public int setPosition(int position) {
		return this.position = position;
	}
	
	// method to set the name of the player
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid name. Name cannot be null or empty.");
        }
        this.name = name;
    }

		
	    public boolean hasUsedHintThisRound() {
	        return usedHintThisRound;
	    }

	    public void setUsedHintThisRound(boolean usedHintThisRound) {
	        this.usedHintThisRound = usedHintThisRound;
	    }
	    
	    public void setBlocked(boolean isBlocked) {
	    	  this.isBlocked = isBlocked;
	    }

		public boolean isBlocked() {
			return isBlocked ;
		}
	

	
	
	
	

}
