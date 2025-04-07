package models;

public class Player {
	
	private String name;
	private int position = 0; // Player's position on the board;
	private int score = 0;
	private int hint = 3; // Number of hints left, default 3
    private boolean usedHintThisRound;
    private int streak = 0; // Number of consecutive correct answers
	
	public Player(String name) {
		setName(name);
		 this.usedHintThisRound = false;
	}
	
	// method to move the player
	public void move(int steps) {
		this.position += steps;
	}
	
	// method to increase the score
	public void increaseScore() {
		score++;
	}
	
    public void useHint() {
        if (hint > 0) {
            hint--;
        }
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
	
	public void setScore(int score) {
		this.score = score;
	}
	
	
	// method to set the name of the player
	public void setName(String name) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		}
		else {
			System.out.println("Invalid name");
		}
	}

		
	    public boolean hasUsedHintThisRound() {
	        return usedHintThisRound;
	    }

	    public void setUsedHintThisRound(boolean usedHintThisRound) {
	        this.usedHintThisRound = usedHintThisRound;
	    }
	

	
	
	
	

}
