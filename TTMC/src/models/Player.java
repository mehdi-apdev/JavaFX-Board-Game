package models;

public class Player {
	
	private String name;
	private int position;
	private int score;
	private int hint; // Number of hints left, default 3
	
	public Player(String name) {
		setName(name);
		this.position = 0;
		this.score = 0;
		this.hint = 3;
	}
	
	// method to move the player
	public void move(int steps) {
		this.position += steps;
	}
	
	// method to increase the score
	public void increaseScore(int points) {
		this.score += points;
	}
	
	// method to use a hint
	public void useHint() {
		this.hint--;
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
	
	
	// method to set the name of the player
	public void setName(String name) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		}
		else {
			System.out.println("Invalid name");
		}
	}
	
	
	
	

}
