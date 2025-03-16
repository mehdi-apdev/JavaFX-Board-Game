package models;

public class SpaceMalus {

	private int number; // number of the space aka position
	private int road; // road number, 1 to 4
	private int id; // id of the square in javafx
	private int malus; // malus value

	public SpaceMalus(int number, int road, int id) {
		this.number = number;
		this.road = road;
		this.id = id;
		this.malus = 3;
	}

	// method to apply the effect of the space
	public void applyEffect(Player player, Game game) {
		player.move(-malus); // player moves back by the malus value
	}

	// method to get the id of the space
	public int getId() {
		return this.id;
	}

	// method to set the id of the space
	public void setId(int id) {
		this.id = id;
	}
	
}
