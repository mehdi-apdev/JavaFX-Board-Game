package models;

public abstract class Space {
	
	@SuppressWarnings("unused")
	private int number; // number of the space aka position
	@SuppressWarnings("unused")
	private int road; // road number, 1 to 4
	private int id; // id of the square in javafx
	
	public Space(int number, int road, int id) {
		this.number = number;
		this.road = road;
		this.id = id;
	}
	
	// method to apply the effect of the space
	
	public abstract void applyEffect(Player player, Game game);
	
	// method to get the id of the space
	public int getId() {
		return this.id;
	}
	
	// method to set the id of the space
	public void setId(int id) {
		this.id = id;
	}

}
