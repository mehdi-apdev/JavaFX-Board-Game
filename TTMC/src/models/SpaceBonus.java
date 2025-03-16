package models;

public class SpaceBonus {

	private int number; // number of the space aka position
	private int road; // road number, 1 to 4
	private int id; // id of the square in javafx
	private int bonus; // bonus value

	public SpaceBonus(int number, int road, int id) {
		this.number = number;
		this.road = road;
		this.id = id;
		this.bonus = 2;
	}

	// method to apply the effect of the space
	public void applyEffect(Player player, Game game) {
		player.move(bonus); // player moves forward by the bonus value
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
